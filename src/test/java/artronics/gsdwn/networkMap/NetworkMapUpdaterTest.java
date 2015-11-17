package artronics.gsdwn.networkMap;

import artronics.gsdwn.helper.FakePacketFactory;
import artronics.gsdwn.node.SdwnNode;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PacketFactory;
import artronics.gsdwn.packet.SdwnPacketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class NetworkMapUpdaterTest
{
    NetworkMap networkMap = new SdwnNetworkMap();
    BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();

    @InjectMocks
    WeightCalculator weightCalculator = new RssiSimpleWeightCalculator();

    FakePacketFactory factory = new FakePacketFactory();

    PacketFactory packetFactory = new SdwnPacketFactory();
    //        mapUpdater = new NetworkMapUpdater(queue, networkMap, weightCalculator);
    NetworkMapUpdater mapUpdater = new NetworkMapUpdater(queue, networkMap, weightCalculator);

    //    NetworkMapUpdater mapUpdater;
    Thread updater = new Thread(mapUpdater);

    SdwnNode node0 = new SdwnNode(0);
    SdwnNode node30 = new SdwnNode(30);
    SdwnNode node35 = new SdwnNode(35);
    SdwnNode node36 = new SdwnNode(36);
    SdwnNode node37 = new SdwnNode(37);

    SdwnNode node40 = new SdwnNode(40);

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void it_should_create_a_graph_with_report_packet() throws InterruptedException
    {
        Packet packet = packetFactory.create(factory.createReportPacket());
        queue.add(packet);
        updater.start();
        Thread.sleep(200);

        NetworkMap expMap = getDefaultNetworkMap();

        assertMapEqual(expMap, networkMap);
        //it should not contain destination node
        assertFalse(networkMap.contains(node0));
    }


    /*
        After sending default packet we send another which says
        node 35 is connected to 36.
        This report is from 35 to 0 and neighbors are 30 and 36
     */
    @Test
    public void it_should_add_nodes_to_current_graph() throws InterruptedException
    {
        Packet packet = createRepPacket();
        Packet packet2 = createRepPacket(35, 0, factory.createNeighbors(30, 36));

        queue.add(packet);
        queue.add(packet2);
        updater.start();
        Thread.sleep(300);

        NetworkMap expMap = getDefaultNetworkMap();
        expMap.addLink(node35, node36, 87);
        assertMapEqual(expMap, networkMap);
    }

    /*
        Now we introduce a new node to network by node 37.
        The address is 40.
     */
    @Test
    public void it_should_add_new_nodes_with_report() throws InterruptedException
    {
        Packet packet = createRepPacket();
        Packet packet2 = createRepPacket(37, 0, factory.createNeighbors(30, 40));

        queue.add(packet);
        queue.add(packet2);
        updater.start();
        Thread.sleep(300);

        NetworkMap expMap = getDefaultNetworkMap();
        expMap.addNode(node40);
        expMap.addLink(node37, node40, 87);
        assertMapEqual(expMap, networkMap);
    }

    /*
        Now by sending some reports we add nodes and links
        Then by sending other reports we simulate the situation
        where a nodes looses its links but it's not island yet
     */
    @Test
    public void it_should_remove_links_and_not_nodes() throws InterruptedException
    {
        Packet packet = createRepPacket();
        Packet packet2 = createRepPacket(35, 0, factory.createNeighbors(30, 36));
        Packet packet3 = createRepPacket(37, 0, factory.createNeighbors(30, 40));

        //Now 35 drops its link from 36 and create a link with 37 and 40
        Packet packet4 = createRepPacket(35, 0, factory.createNeighbors(30, 37, 40));


        queue.add(packet);
        queue.add(packet2);
        queue.add(packet3);
        queue.add(packet4);
        updater.start();
        Thread.sleep(500);

        NetworkMap expMap = getDefaultNetworkMap();
        expMap.addNode(node40);
        expMap.addLink(node37, node40, 87);
        expMap.addLink(node35, node37, 87);
        expMap.addLink(node35, node40, 87);
        assertMapEqual(expMap, networkMap);
    }

    @Test
    public void it_should_detect_island_node_and_remove_it() throws InterruptedException
    {
        Packet packet = createRepPacket();
        Packet packet2 = createRepPacket(30, 0, factory.createNeighbors(35, 36));
        queue.add(packet);
        queue.add(packet2);
        updater.start();
        Thread.sleep(300);

        NetworkMap expMap = getDefaultNetworkMap();
        expMap.removeNode(node37);

        assertMapEqual(expMap, networkMap);
    }

    /*
        It is not possible for the network to have unconnected graph
        however, at the beginning of network formation it's likely to
        receive packets from different parts of the network.
     */
    @Test
    public void it_should_create_a_not_connected_graph() throws InterruptedException
    {
        Packet packet = createRepPacket();
        Packet packet2 = createRepPacket(40, 0, factory.createNeighbors(41));
        queue.add(packet);
        queue.add(packet2);
        updater.start();
        Thread.sleep(300);

        NetworkMap expMap = getDefaultNetworkMap();
        SdwnNode node41 = new SdwnNode(41);
        expMap.addNode(node40);
        expMap.addNode(node41);
        expMap.addLink(node40, node41, 65);

        assertMapEqual(expMap, networkMap);
    }

    private void assertMapEqual(NetworkMap exp, NetworkMap act)
    {
        assertEquals(exp.getNetworkGraph().vertexSet(), act.getNetworkGraph().vertexSet());
        //I use toString because actual equal override is considered as weighted
        //however here i do not consider weigh of each link
        //For this to work you should construct your link exactly in order
        assertEquals(exp.getNetworkGraph().edgeSet().toString(),
                     act.getNetworkGraph().edgeSet().toString());
    }

    //this returns a netMap correspond to default ReportPacket
    //which FakePacketFactory produces
    private NetworkMap getDefaultNetworkMap()
    {
        NetworkMap expMap = new SdwnNetworkMap();
        expMap.addNode(node30);
        expMap.addNode(node35);
        expMap.addNode(node36);
        expMap.addNode(node37);
        expMap.addLink(node30, node35, 45);
        expMap.addLink(node30, node36, 45);
        expMap.addLink(node30, node37, 45);
        return expMap;
    }

    private Packet createRepPacket()
    {
        return packetFactory.create(factory.createReportPacket());
    }

    private Packet createRepPacket(int src, int dst, List<Integer> neighbors)
    {
        return packetFactory.create(factory.createReportPacket(src, dst, 1, 255, neighbors));
    }

}