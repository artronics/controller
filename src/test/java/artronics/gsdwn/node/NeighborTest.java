package artronics.gsdwn.node;

import artronics.gsdwn.helper.FakePacketFactory;
import artronics.gsdwn.packet.SdwnReportPacket;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

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

    @Test
    public void it_should_be_equal_to_a_Node_just_if_addresses_are_the_same(){
        SdwnNode node = new SdwnNode(1);
        Neighbor neighbor = new Neighbor(1,23);

        assertThat(node,equalTo(neighbor));
    }

    @Test
    public void it_should_work_in_HashSet_contains_methods(){
        //we add a node and a neighbor with same address
        //HashSet must be contains one of them as soon as
        //generic type is Node
        SdwnNode node0= new SdwnNode(0);
        Neighbor neighbor0 = new Neighbor(0,32);
        Set<Node> set = new HashSet<>();
        set.add(node0);
        set.add(neighbor0);

        assertThat(set.size(),equalTo(1));
        assertTrue(set.contains(neighbor0));
        assertTrue(set.contains(node0));
    }
}