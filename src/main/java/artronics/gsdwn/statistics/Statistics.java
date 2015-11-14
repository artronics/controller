package artronics.gsdwn.statistics;

import artronics.gsdwn.packet.Packet;

public interface Statistics extends Runnable
{
    void addPacket(Packet packet);

    StatisticsImpl.PerNodeStatistics forNode(int address);
}
