package artronics.gsdwn.statistics;

import artronics.gsdwn.helper.FakePacketFactory;
import artronics.gsdwn.packet.SdwnReportPacket;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatisticsImplTest
{
    FakePacketFactory factory = new FakePacketFactory();

    Statistics statistics = new StatisticsImpl();

    @Test
    public void test_recording_battery_info_carried_with_report_Packet()
    {
        SdwnReportPacket packet = new SdwnReportPacket(factory.createReportPacket());
        statistics.addPacket(packet);
        statistics.run();
        int batt = statistics.forNode(0).getBattery();

        assertEquals(255, batt);
    }
}