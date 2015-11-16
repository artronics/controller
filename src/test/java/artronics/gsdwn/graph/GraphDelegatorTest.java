package artronics.gsdwn.graph;

import artronics.gsdwn.networkMap.NetworkMap;
import artronics.gsdwn.networkMap.SdwnNetworkMap;
import artronics.gsdwn.node.Node;
import artronics.gsdwn.node.SdwnNode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class GraphDelegatorTest
{
    NetworkMap networkMap;
    GraphDelegator graphHelper;

    Node node0;
    Node node1;
    Node node2;
    Node node3;

    @Before
    public void setUp() throws Exception
    {
        networkMap = new SdwnNetworkMap();

        graphHelper = new GraphDelegator(networkMap.getNetworkGraph());

        /*
            A simple graph for testing GraphHelper
            node0 --30--> node1
            node2 --05--> node1
            node2 --10--> node0
            node3 --50--> node1

            The shortest path from node0 to node3 is:
                node0
                node2
                node1
                node3
            Remember NetworkMap is not a directed graph.
         */

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
    }

    @Test
    public void It_should_give_the_shortest_path()
    {
        List<Node> path = graphHelper.getShortestPath(node0, node3);

        assertThat(path.size(), equalTo(4));
    }

    @Test
    public void It_should_give_a_list_of_nodes_rigth_order_contains_source_target()
    {
        List<Node> path = graphHelper.getShortestPath(node0, node3);

        Node targetNode0 = path.get(0);
        Node targetNode2 = path.get(1);
        Node targetNode1 = path.get(2);
        Node targetNode3 = path.get(3);

        assertThat(targetNode0, equalTo(node0));
        assertThat(targetNode2, equalTo(node2));
        assertThat(targetNode1, equalTo(node1));
        assertThat(targetNode3, equalTo(node3));
    }

    /*
        In this test I created a simple graph with 3 vertices.
        The goal is to test the order of src and dst in simple fully
        connected graph.
     */
    @Test
    public void test_for_simple_graph()
    {
        NetworkMap map = new SdwnNetworkMap();
        map.addNode(node0);
        map.addNode(node1);
        map.addNode(node2);

        map.addLink(node0, node1, 40);
        map.addLink(node0, node2, 30);
        map.addLink(node1, node2, 30);

        GraphDelegator helper = new GraphDelegator(map.getNetworkGraph());
        List<Node> path = helper.getShortestPath(node1, node0);

        Node targetNode0 = path.get(0);
        Node targetNode1 = path.get(1);

        assertThat(targetNode0, equalTo(node1));
        assertThat(targetNode1, equalTo(node0));
    }

    @Test
    public void it_should_return_a_set_of_neighbors_not_containing_itself()
    {
        Set<Node> nodes = graphHelper.getNeighbors(node2);

        //should not contain itself
        assertFalse(nodes.contains(node2));

        assertTrue(nodes.contains(node0));
        assertTrue(nodes.contains(node1));
        assertFalse(nodes.contains(node3));
    }
}