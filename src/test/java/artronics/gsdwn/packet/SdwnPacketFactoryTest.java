package artronics.gsdwn.packet;

import artronics.gsdwn.helper.FakePacketFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SdwnPacketFactoryTest
{
    FakePacketFactory fakeFactory = new FakePacketFactory();
    PacketFactory packetFactory = new SdwnPacketFactory();

    List<Integer> rawPacket = new ArrayList<>();

    @Test
    public void it_should_create_data_packet()
    {
        rawPacket = fakeFactory.createRawDataPacket();
        Packet packet = packetFactory.create(rawPacket);

        assertPacketEquals(fakeFactory.createDataPacket(), packet);
    }

    @Test
    public void it_should_create_RuleRequestPacket()
    {
        rawPacket = fakeFactory.createRawRuleRequestPacket(0, 30, 10);
        Packet packet = packetFactory.create(rawPacket);

        assertPacketEquals(fakeFactory.createRuleRequestPacket(0, 30, 10), packet);
    }

    @Test
    public void it_should_create_ReportPacket()
    {
        rawPacket = fakeFactory.createRawReportPacket(30, 0,
                                                      Arrays.asList(0, 30, 234));
        Packet packet = packetFactory.create(rawPacket);

        assertPacketEquals(fakeFactory.createReportPacket(30, 0,
                                                          Arrays.asList(0, 30, 234)), packet);
    }

    private void assertPacketEquals(Packet expPacket, Packet actPacket)
    {
        List<Integer> expContent = expPacket.getContent();
        List<Integer> actContent = actPacket.getContent();
        assertEquals(expContent, actContent);
    }
}