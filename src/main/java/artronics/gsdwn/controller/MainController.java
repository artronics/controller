package artronics.gsdwn.controller;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.networkMap.NetworkMap;
import artronics.gsdwn.networkMap.NetworkMapUpdater;
import artronics.gsdwn.networkMap.SdwnNetworkMap;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PacketFactory;
import artronics.gsdwn.packet.SdwnPacketFactory;
import artronics.gsdwn.packet.SdwnReportPacket;
import artronics.gsdwn.statistics.Statistics;
import artronics.gsdwn.statistics.StatisticsImpl;

import java.util.LinkedList;
import java.util.List;

public class MainController implements Controller, Runnable
{
    private final Chapar chapar = new Chapar();

    private final LinkedList<List<Integer>> rxMessage = chapar.getReceivedBuffer();
    private final LinkedList<List<Integer>> txMessage = chapar.getTransmitBuffer();

    private final PacketFactory packetFactory = new SdwnPacketFactory();

    private final NetworkMap networkMap = new SdwnNetworkMap();
    private final NetworkMapUpdater mapUpdater = new NetworkMapUpdater(networkMap);
    private final Statistics statistics = new StatisticsImpl();

    private volatile boolean isStarted = false;

    public MainController()
    {
    }

    public void start() throws ChaparConnectionException
    {
        chapar.connect();

        Thread cp = new Thread(chapar, "Chapar");
        Thread st = new Thread(statistics, "Statistics");
        Thread mu = new Thread(mapUpdater, "MapUpdater");

        cp.start();
        st.start();
        mu.start();

        isStarted = true;
    }

    @Override
    public void run()
    {
        while (isStarted) {
            while (!rxMessage.isEmpty()) {
                Packet packet = packetFactory.create(rxMessage.poll());
                processPacket(packet);
            }
        }
    }

    @Override
    public void processPacket(Packet packet)
    {
        statistics.addPacket(packet);

        switch (packet.getType()) {
            case REPORT:
                SdwnReportPacket rPacket = (SdwnReportPacket) packet;

                mapUpdater.addReportPacket(rPacket);
                processReportPacket(rPacket);

                break;
        }

    }

    private void processReportPacket(SdwnReportPacket packet)
    {

    }
}
