package artronics.gsdwn.statistics;

import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PoisonPacket;
import artronics.gsdwn.packet.SdwnReportPacket;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class StatisticsImpl implements Statistics
{
    private final static Packet POISON_PILL = new PoisonPacket();
    private final BlockingQueue<Packet> queue;

    private final HashMap<Integer, PerNodeStatistics> nodes = new HashMap<>();

    public StatisticsImpl(BlockingQueue<Packet> queue)
    {
        this.queue = queue;
    }

    @Override
    public void stop()
    {
        queue.add(POISON_PILL);
    }

    @Override
    public void run()
    {
        while (true) {
            try {
                Packet packet = queue.take();
                if (packet == POISON_PILL)
                    break;

                int addr = packet.getSource();
                PerNodeStatistics st = null;

                if (!nodes.containsKey(addr)) {
                    st = new PerNodeStatistics();
                    nodes.put(addr, st);
                }else {
                    st = nodes.get(addr);
                }

                processPacket(packet, st);

            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public PerNodeStatistics forNode(int address)
    {
        return nodes.get(address);
    }

    private void processPacket(Packet packet, PerNodeStatistics st)
    {
        switch (packet.getType()) {
            case REPORT:
                evaluateReportPacket((SdwnReportPacket) packet, st);
        }
    }

    private void evaluateReportPacket(SdwnReportPacket packet, PerNodeStatistics st)
    {
        SdwnReportPacket rPacket = packet;
        st.battery = rPacket.getBattery();
        System.out.println(Thread.currentThread().getName() + "bat " + st.battery);
    }

    class PerNodeStatistics
    {
        private int battery;

        public int getBattery()
        {
            return battery;
        }
    }

}
