package artronics.gsdwn.controller;

import artronics.chaparMini.DeviceConnection;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.networkMap.*;
import artronics.gsdwn.node.Node;
import artronics.gsdwn.node.SdwnNode;
import artronics.gsdwn.packet.*;
import artronics.gsdwn.statistics.Statistics;
import artronics.gsdwn.statistics.StatisticsImpl;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SdwnController implements Controller
{
    private final static Packet POISON_PILL = new PoisonPacket();

    private final DeviceConnection deviceConnection;

    private final BlockingQueue<List<Integer>> chpRxMsg;
    private final BlockingQueue<List<Integer>> chpTxMsg;

    private final BlockingQueue<Packet> cntRxPackets = new LinkedBlockingQueue<>();
    private final BlockingQueue<Packet> cntTxPackets = new LinkedBlockingQueue<>();

    //For Statistics
    private final BlockingQueue<Packet> stcPackets = new LinkedBlockingQueue<>();

    //For NetworkMapUpdater
    private final BlockingQueue<Packet> mapPackets = new LinkedBlockingQueue<>();
    private final WeightCalculator weightCalculator = new RssiSimpleWeightCalculator();

    private final PacketFactory packetFactory = new SdwnPacketFactory();

    private final NetworkMap networkMap = new SdwnNetworkMap();

    private final ShortestPathFinder pathFinder = new SdwnShortestPathFinder(networkMap);

    private final NetworkMapUpdater mapUpdater =
            new NetworkMapUpdater(mapPackets, networkMap, weightCalculator);

    private final Statistics statistics = new StatisticsImpl(stcPackets);

    private final Runnable packetBroker = new Runnable()
    {
        @Override
        public void run()
        {
            while (true) {
                try {
                    Packet packet = cntRxPackets.take();
                    if (packet == POISON_PILL)
                        break;

                    mapPackets.add(packet);
                    stcPackets.add(packet);

                    processPacket(packet);

                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private final Runnable msgBroker = new Runnable()
    {
        @Override
        public void run()
        {
            while (true){
                try {
                    List<Integer> msg = chpRxMsg.take();

                    Packet packet = packetFactory.create(msg);
                    cntRxPackets.add(packet);


                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /*
        Sdwn Controller has nothing to do with received packets
        that it takes from outside (cntTxPackets). It just gets the
        content of packet and pass it to deviceConnection.
     */
    private final Runnable cntTxListener = new Runnable()
    {
        @Override
        public void run()
        {
            while (true){
                try {
                    Packet packet = cntTxPackets.take();

                    List<Integer> msg = packet.getContent();
                    chpTxMsg.add(msg);

                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public SdwnController(DeviceConnection deviceConnection)
    {
        this.deviceConnection = deviceConnection;
        chpRxMsg = this.deviceConnection.getRxQueue();
        chpTxMsg = this.deviceConnection.getTxQueue();
    }

    @Override
    public void start() throws ChaparConnectionException
    {
        deviceConnection.connect();
        deviceConnection.start();

        Thread msgBrThr = new Thread(msgBroker,"MsgBroker");
        Thread pckBrThr = new Thread(packetBroker,"PckBroker");
        Thread pckLstThr = new Thread(cntTxListener,"CntTxListener");
        Thread mapUpThr = new Thread(mapUpdater, "MapUpdater");
        Thread stThr = new Thread(statistics, "Statistics");

        msgBrThr.start();
        pckBrThr.start();
        pckLstThr.start();
        mapUpThr.start();
        stThr.start();
    }


    @Override
    public void processPacket(Packet packet)
    {

        switch (packet.getType()) {
            case REPORT:
                SdwnReportPacket rPacket = (SdwnReportPacket) packet;
                processReportPacket(rPacket);
                break;
            case RULE_REQUEST:
                SdwnRuleRequestPacket rq = (SdwnRuleRequestPacket) packet;
                processRuleRequestPacket(rq);
                break;
        }

    }

    private void processRuleRequestPacket(SdwnRuleRequestPacket rq)
    {
        SdwnNode srcNode = new SdwnNode(rq.getSrcShortAddress());
        SdwnNode dstNode = new SdwnNode(rq.getDstShortAddress());

//        List<Node> nodes = pathFinder.getShortestPath(srcNode, dstNode);
        List<Node> nodes = pathFinder.getShortestPath(dstNode, srcNode);
        nodes.add(0,srcNode);
        nodes.add(dstNode);

        Packet packet= SdwnOpenPathPacket.create(nodes);

        cntTxPackets.add(packet);

    }

    private void processReportPacket(SdwnReportPacket packet)
    {

    }

    @Override
    public void stop()
    {
        deviceConnection.stop();
        cntRxPackets.add(POISON_PILL);
        statistics.stop();
        mapUpdater.stop();
    }

    public BlockingQueue<Packet> getCntRxPackets()
    {
        return cntRxPackets;
    }

    public BlockingQueue<Packet> getCntTxPackets()
    {
        return cntTxPackets;
    }
}
