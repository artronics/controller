package artronics.gsdwn.controller;

import artronics.chaparMini.DeviceConnection;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.log.Log;
import artronics.gsdwn.log.SdwnPacketLogger;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.networkMap.NetworkMap;
import artronics.gsdwn.networkMap.NetworkMapUpdater;
import artronics.gsdwn.networkMap.SdwnShortestPathFinder;
import artronics.gsdwn.networkMap.ShortestPathFinder;
import artronics.gsdwn.node.Node;
import artronics.gsdwn.node.SdwnNode;
import artronics.gsdwn.packet.*;
import artronics.gsdwn.statistics.Statistics;
import artronics.gsdwn.statistics.StatisticsImpl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//@Entity
//@Table(name = "sdwn_controller")
//@DiscriminatorValue(value = "cnt")
public class SdwnController extends ControllerConfig implements Controller
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
    private final BlockingQueue<Packet> mapUpdaterQueue;
    private final PacketFactory packetFactory = new SdwnPacketFactory();
    private final NetworkMap networkMap;
    private final ShortestPathFinder pathFinder;
    private final NetworkMapUpdater mapUpdater;
    private final Statistics statistics = new StatisticsImpl(stcPackets);
    private final Runnable msgBroker = new Runnable()
    {
        @Override
        public void run()
        {
            while (true) {
                try {
                    final List<Integer> msg = chpRxMsg.take();

                    final Packet packet = packetFactory.create(msg);
                    if (packet == null) {
                        Log.PACKET.error(new SdwnPacketLogger().logPacket(msg));
                    }
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
            while (true) {
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
    private ControllerConfig config;
    private Integer sinkAddress = 0;
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

                    mapUpdaterQueue.add(packet);
                    stcPackets.add(packet);

                    processPacket(packet);

                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public SdwnController(DeviceConnection deviceConnection, NetworkMap networkMap,
                          NetworkMapUpdater mapUpdater)
    {
        this(0, deviceConnection, networkMap, mapUpdater);
    }

    public SdwnController(Integer sinkAddress, DeviceConnection deviceConnection,
                          NetworkMap networkMap,
                          NetworkMapUpdater mapUpdater)
    {
        this.sinkAddress = sinkAddress;
        this.deviceConnection = deviceConnection;
        this.networkMap = networkMap;
        this.mapUpdater = mapUpdater;

        this.pathFinder = new SdwnShortestPathFinder(networkMap);

        chpRxMsg = this.deviceConnection.getChaparRxQueue();
        chpTxMsg = this.deviceConnection.getChaparTxQueue();

        mapUpdaterQueue = this.mapUpdater.getPacketQueue();

        //Add sink node as the first node in network.
        this.networkMap.addNode(new SdwnNode(sinkAddress));
        Log.SDWN.debug("Add Sink [address: " + sinkAddress + " ]");
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
            case DATA:
                SdwnDataPacket dataPacket = (SdwnDataPacket) packet;
                processDataPacket(dataPacket);
                break;
            case REPORT:
                SdwnReportPacket rPacket = (SdwnReportPacket) packet;
                processReportPacket(rPacket);
                break;
            case RL_REQ:
                SdwnRuleRequestPacket rq = (SdwnRuleRequestPacket) packet;
                processRuleRequestPacket(rq);
                break;
        }

    }

    private void processDataPacket(SdwnDataPacket dataPacket)
    {
    }

    private void processRuleRequestPacket(SdwnRuleRequestPacket rq)
    {
        SdwnNode srcNode = new SdwnNode(rq.getSrcShortAddress());
        SdwnNode dstNode = new SdwnNode(rq.getDstShortAddress());

//        List<Node> nodes = pathFinder.getShortestPath(srcNode, dstNode);
        List<Node> nodes = pathFinder.getShortestPath(dstNode, srcNode);

        Packet response;
        if (srcNode.getAddress() == sinkAddress) {
            Collections.reverse(nodes);
            response = SdwnOpenPathPacket.create(sinkAddress, dstNode.getAddress(), nodes);
        }else {
            //src and dst are extracted from nodes
            response = SdwnOpenPathPacket.create(nodes);
        }

        cntTxPackets.add(response);
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

    @Override
    public void setConfig(ControllerConfig controllerConfig)
    {
        this.config = controllerConfig;
    }

    @Override
    public BlockingQueue<Packet> getCntRxPacketsQueue()
    {
        return cntRxPackets;
    }

    @Override
    public BlockingQueue<Packet> getCntTxPacketsQueue()
    {
        return cntTxPackets;
    }
}
