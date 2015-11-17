package artronics.gsdwn.statistics;

import artronics.gsdwn.helper.FakePacketFactory;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.SdwnReportPacket;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;

public class StatisticsImplTest
{
    FakePacketFactory factory = new FakePacketFactory();

    Statistics statistics;
    BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();

    @Before
    public void setUp() throws Exception
    {
        SdwnReportPacket packet = new SdwnReportPacket(factory.createReportPacket());
        queue.add(packet);
        statistics = new StatisticsImpl(queue);
        Thread stcThr = new Thread(statistics);
        stcThr.start();

        //Sleep so statistics can be run so it has value for assertion.
        Thread.sleep(100);
    }

    @Test
    public void test_recording_battery_info_carried_with_report_Packet() throws InterruptedException
    {
        int batt = statistics.forNode(30).getBattery();
        statistics.stop();

        assertEquals(255, batt);
    }
}