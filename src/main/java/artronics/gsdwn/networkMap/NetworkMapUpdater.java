package artronics.gsdwn.networkMap;

import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PoisonPacket;

import java.util.concurrent.BlockingQueue;

public class NetworkMapUpdater implements Runnable
{
    private final static Packet POISON_PILL = new PoisonPacket();

    private final BlockingQueue<Packet> queue;
    private final NetworkMap networkMap;

    private volatile boolean isStarted = true;

    public NetworkMapUpdater(BlockingQueue<Packet> queue, NetworkMap networkMap)
    {
        this.queue = queue;
        this.networkMap = networkMap;
    }


    @Override
    public void run()
    {
        while (true) {
            try {
                Packet packet = queue.take();
                if (packet == POISON_PILL)
                    break;
                updateMap(packet);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMap(Packet packet)
    {
        switch (packet.getType()) {
            case REPORT:
                System.out.println(Thread.currentThread().getName() + " update map");
        }
    }

    public void stop()
    {
        queue.add(POISON_PILL);
    }
}
