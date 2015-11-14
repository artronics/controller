package artronics.gsdwn.networkMap;

import artronics.gsdwn.packet.SdwnReportPacket;

import java.util.LinkedList;

public class NetworkMapUpdater implements Runnable
{
    private final NetworkMap networkMap;
    private final LinkedList<SdwnReportPacket> queue = new LinkedList<>();

    public NetworkMapUpdater(NetworkMap networkMap)
    {
        this.networkMap = networkMap;
    }

    public void addReportPacket(SdwnReportPacket packet)
    {
        queue.add(packet);
    }


    @Override
    public void run()
    {
        while (!queue.isEmpty()) {
            updateMap(queue.poll());
        }
    }

    private void updateMap(SdwnReportPacket packet)
    {

    }
}
