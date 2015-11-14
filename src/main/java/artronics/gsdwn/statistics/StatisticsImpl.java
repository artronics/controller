package artronics.gsdwn.statistics;

import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.SdwnReportPacket;

import java.util.HashMap;
import java.util.LinkedList;

public class StatisticsImpl implements Statistics
{
    private final HashMap<Integer, PerNodeStatistics> nodes = new HashMap<>();

    private LinkedList<Packet> queue = new LinkedList<>();

    @Override
    public void run()
    {
        while (!queue.isEmpty()) {
            Packet packet = queue.poll();
            int addr = packet.getSource();
            PerNodeStatistics st = null;

            if (!nodes.containsKey(addr)) {
                st = new PerNodeStatistics();
                nodes.put(addr, st);
            }else {
                st = nodes.get(addr);
            }

            processPacket(packet, st);
        }
    }

    @Override
    public void addPacket(Packet packet)
    {
        queue.add(packet);
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
