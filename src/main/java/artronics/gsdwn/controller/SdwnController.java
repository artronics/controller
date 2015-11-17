package artronics.gsdwn.controller;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.networkMap.*;
import artronics.gsdwn.packet.*;
import artronics.gsdwn.statistics.Statistics;
import artronics.gsdwn.statistics.StatisticsImpl;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SdwnController implements Controller
{
    private final static Packet POISON_PILL = new PoisonPacket();

    private final Chapar chapar = new Chapar();

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
        content of packet and pass it to chapar.
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

    public SdwnController()
    {
        chpRxMsg = chapar.getRxMessages();
        chpTxMsg = chapar.getTxMessages();
    }

    public void start() throws ChaparConnectionException
    {
        chapar.connect();
        chapar.start();

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
        }

    }

    private void processReportPacket(SdwnReportPacket packet)
    {

    }

    public void stop()
    {
        chapar.stop();
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
