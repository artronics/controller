package artronics.gsdwn.networkMap;

import artronics.gsdwn.node.Node;
import artronics.gsdwn.node.SdwnNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class SdwnNetworkMapTest
{
    NetworkMap networkMap;

    Node node0;
    Node node1;
    Node node2;
    Node node3;

    List<Node> nodes = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        networkMap = new SdwnNetworkMap();

        node0 = new SdwnNode(0);
        node1 = new SdwnNode(1);
        node2 = new SdwnNode(2);
        node3 = new SdwnNode(3);

        networkMap.addNode(node0);
        networkMap.addNode(node1);
        networkMap.addNode(node2);
        networkMap.addNode(node3);

        networkMap.addLink(node0, node1, 30);
        networkMap.addLink(node2, node1, 5);
        networkMap.addLink(node2, node0, 10);
        networkMap.addLink(node3, node1, 50);

        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
    }

    @Test
    public void
    If_we_add_link_to_a_node_which_already_exists_it_should_ignore_it()
    {
        //null check on addLink otherwise jgrapht throws exp
        networkMap.addLink(node0, node2, 10);
    }

    @Test
    public void It_should_return_true_if_a_node_hasLink_to_other_node()
    {
        assertThat(networkMap.hasLink(node0, node1), equalTo(true));
        //reverse dir
        assertThat(networkMap.hasLink(node1, node0), equalTo(true));

        assertThat(networkMap.hasLink(node0, node3), equalTo(false));

        assertFalse(networkMap.hasLink(node0, node3));
    }

    @Test
    public void It_should_return_false_if_we_ask_a_node_hasLink_to_itself()
    {
        assertThat(networkMap.hasLink(node0, node0), equalTo(false));
    }

    @Test
    public void Test_contains_node()
    {
        assertThat(networkMap.contains(node0), equalTo(true));
    }

    @Test
    public void test_remove_node()
    {
        networkMap.removeNode(node3);
        assertFalse(networkMap.contains(node3));
    }

    @Test
    public void test_remove_link(){
        networkMap.removeLink(node1,node2);
        assertFalse(networkMap.hasLink(node1,node2));
    }
    @Test
    public void test_remove_link_in_reverse(){
        networkMap.removeLink(node2,node1);
        assertFalse(networkMap.hasLink(node1,node2));
    }

    @Test
    public void Two_nodes_with_same_address_are_equal()
    {
        Node eqNode0 = new SdwnNode(0);
        assertThat(networkMap.contains(eqNode0), equalTo(true));
    }

    @Test
    public void test_getAllNodes()
    {
        assertThat(networkMap.getAllNodes(), equalTo(nodes));
    }
}