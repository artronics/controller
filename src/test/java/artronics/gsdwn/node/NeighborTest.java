package artronics.gsdwn.node;

import artronics.gsdwn.helper.FakePacketFactory;
import artronics.gsdwn.packet.SdwnReportPacket;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class NeighborTest
{

    FakePacketFactory factory = new FakePacketFactory();
    List<Integer> packetBytes;
    SdwnReportPacket packet;
    List<Neighbor> neighbors;

    @Before
    public void setUp() throws Exception
    {
        packetBytes = factory.createReportPacket();
        packet = new SdwnReportPacket(packetBytes);
        neighbors = Neighbor.createNeighbors(packet);
    }

    @Test
    public void it_should_create_neighbors_by_passing_reportPacket()
    {

        assertThat(neighbors.size(), equalTo(3));
        assertThat(neighbors.get(0).getAddress(), equalTo(35));
        assertThat(neighbors.get(1).getAddress(), equalTo(36));
        assertThat(neighbors.get(2).getAddress(), equalTo(37));
    }

    @Test
    public void test_up_casting_to_node()
    {
        SdwnNode node = neighbors.get(0);
        SdwnNode sameNode = new SdwnNode(35);

        assertEquals(sameNode, node);
    }
}