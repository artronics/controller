package artronics.gsdwn.controller;

import artronics.gsdwn.networkMap.NetworkMap;
import artronics.gsdwn.networkMap.NetworkMapUpdater;
import artronics.gsdwn.networkMap.SdwnNetworkMap;
import artronics.gsdwn.packet.*;
import artronics.gsdwn.statistics.Statistics;
import artronics.gsdwn.statistics.StatisticsImpl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SdwnController implements Controller
{
    private final static Packet POISON_PILL = new PoisonPacket();

    private final BlockingQueue<Packet> cntRxPackets;
    private final BlockingQueue<Packet> cntTxPackets;

    //For Statistics
    private final BlockingQueue<Packet> stcPackets = new LinkedBlockingQueue<>();

    //For NetworkMapUpdater
    private final BlockingQueue<Packet> mapPackets = new LinkedBlockingQueue<>();

    private final PacketFactory packetFactory = new SdwnPacketFactory();

    private final NetworkMap networkMap = new SdwnNetworkMap();
    private final NetworkMapUpdater mapUpdater = new NetworkMapUpdater(mapPackets, networkMap);
    private final Statistics statistics = new StatisticsImpl(stcPackets);
    public String kir;
    private volatile boolean isStarted = false;

    public SdwnController(
            BlockingQueue<Packet> cntRxPackets,
            BlockingQueue<Packet> cntTxPackets)
    {
        this.cntRxPackets = cntRxPackets;
        this.cntTxPackets = cntTxPackets;

        Thread mapUpThr = new Thread(mapUpdater, "MapUpdater");
        Thread stThr = new Thread(statistics, "Statistics");
        mapUpThr.start();
        stThr.start();
    }

    @Override
    public void run()
    {
        while (true) {
            try {
                kir = "foo";
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
}
