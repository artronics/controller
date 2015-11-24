package artronics.gsdwn.packet;

import artronics.gsdwn.node.Node;
import artronics.gsdwn.node.SdwnNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SdwnOpenPathPacketTest
{
    SdwnOpenPathPacket packet;
    List<Node> nodes = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        nodes.add(new SdwnNode(0));
        nodes.add(new SdwnNode(1));
        nodes.add(new SdwnNode(2));
        nodes.add(new SdwnNode(3));
        packet = SdwnOpenPathPacket.create(nodes);
    }

    @Test
    public void should_create_with_a_list_of_nodes(){
        assertThat(packet.getSrcShortAddress(),equalTo(0));//source
        assertThat(packet.getDstShortAddress(),equalTo(3));//destination

        assertThat(packet.getType(), equalTo(SdwnPacketType.OPN_PT));
    }

    @Test
    public void payload_should_contains_the_path(){
        List<Integer> addrs = SdwnOpenPathPacket.getNodesAddresses(packet.getContent());
        assertThat(addrs.size(), equalTo(4));
        assertThat(addrs.get(0), equalTo(0));
        assertThat(addrs.get(1), equalTo(1));
        assertThat(addrs.get(2), equalTo(2));
        assertThat(addrs.get(3), equalTo(3));
    }

}