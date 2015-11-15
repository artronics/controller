package artronics.gsdwn.networkMap;

import artronics.gsdwn.packet.SdwnReportPacket;

import java.util.LinkedList;

public class NetworkMapUpdater implements Runnable
{
    private final NetworkMap networkMap;
    private final LinkedList<SdwnReportPacket> queue = new LinkedList<>();

    private volatile boolean isStarted = true;

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
        while (isStarted) {
            while (!queue.isEmpty()) {
                updateMap(queue.poll());
            }
        }
    }

    private void updateMap(SdwnReportPacket packet)
    {
        System.out.println(Thread.currentThread().getName() + " update map");
    }
}
