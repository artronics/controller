package artronics.gsdwn.statistics;

public interface Statistics extends Runnable
{
    StatisticsImpl.PerNodeStatistics forNode(int address);

    void stop();
}
