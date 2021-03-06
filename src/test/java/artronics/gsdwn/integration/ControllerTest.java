package artronics.gsdwn.integration;

import artronics.chaparMini.DeviceConnection;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.controller.SdwnController;
import artronics.gsdwn.helper.FakePacketFactory;
import artronics.gsdwn.networkMap.*;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.SdwnDataPacket;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;

public class ControllerTest
{
    private static final int SINK_ADDR = 0;

    DeviceConnection mockDevCon = new MockDeviceConnection();
    WeightCalculator weightCalculator = new FixedWeightCalculator(100);
    NetworkMap networkMap = new SdwnNetworkMap();
    NetworkMapUpdater mapUpdater = new NetworkMapUpdater(networkMap, weightCalculator);
    Controller controller = new SdwnController(mockDevCon, networkMap, mapUpdater);

    FakePacketFactory factory = new FakePacketFactory();

    BlockingQueue<Packet> cntRxQ;
    BlockingQueue<Packet> cntTxQ;

    BlockingQueue<List<Integer>> deviceRxQ;
    BlockingQueue<List<Integer>> deviceTxQ;


    List<Integer> actPacket;
    List<Integer> expPacket;

    /*
        Here is the scenario. First we construct a simple graph. It looks
        like this. Sink is only connected to node 30. node 30 is connected to 35,36,37 and node
        300 is connected
         to 35,36,37 as well. Controller send Data to 300. Then each test
         will accordingly assert controller response.

     */
    @Before
    public void setUp() throws Exception
    {
        cntRxQ = controller.getCntRxPacketsQueue();
        cntTxQ = controller.getCntTxPacketsQueue();

        deviceRxQ = mockDevCon.getChaparRxQueue();
        deviceTxQ = mockDevCon.getChaparTxQueue();

        controller.start();

        //First we send a report from Sink
        deviceRxQ.add(factory.createRawReportPacket(SINK_ADDR,
                                                    SINK_ADDR,
                                                    factory.createNeighbors(30)));
        //Send two report packet to construct the graph.
        deviceRxQ.add(factory.createRawReportPacket(30,
                                                    SINK_ADDR,
                                                    factory.createNeighbors(0, 35, 36, 37)));
        // node 300 is connected to 35,36,37
        deviceRxQ.add(factory.createRawReportPacket(300,
                                                    SINK_ADDR,
                                                    factory.createNeighbors(35, 36, 37)));

        //controller send data to 300
        // 10 is the data length and it doesn't matter what is its value
        //Since this process is just related to DeviceConnection
        //at this point we assume that sink already has received our
        //data packet

        //give it some time so mapUpdater can construct the graph
        Thread.sleep(400);

    }

    /*
        When we send a data packet controller must take the content of
        that packet and send it as it is.
     */
    @Test
    public void test_sending_data() throws InterruptedException
    {
        SdwnDataPacket packet = SdwnDataPacket.create(SINK_ADDR, 300, 10);
        cntTxQ.add(packet);

        actPacket = deviceTxQ.take();
        expPacket = factory.createRawDataPacket(SINK_ADDR, 300, 10);

        assertEquals(expPacket, actPacket);
    }

//    @Test
//    public void test_sending_data_in_loop() throws InterruptedException
//    {
//        SdwnDataPacket packet = SdwnDataPacket.create(SINK_ADDR,300,10);
//        cntTxQ.add(packet);
//
//        actPacket = deviceTxQ.take();
//        expPacket = factory.createRawDataPacket(SINK_ADDR,300,10);
//
//        assertEquals(expPacket,actPacket);
//    }


    /*
        Now Controller should send OpenPath packet in response to RuleReq
     */
    @Test
    public void it_should_send_OpenPath_for_sink_node() throws InterruptedException
    {
        //Sink will response with RuleRequest
        deviceRxQ.add(factory.createRawRuleRequestPacket(SINK_ADDR, 300, 10));
        actPacket = deviceTxQ.take();

        expPacket = factory.createRawOpenPathPacket(SINK_ADDR,
                                                    300,
                                                    Arrays.asList(SINK_ADDR, 30, 35, 300));

        assertEquals(expPacket, actPacket);
    }

    @Test
    public void it_should_send_OpenPath_for_other_nodes_than_sink() throws InterruptedException
    {
        //Sink will response with RuleRequest
        deviceRxQ.add(factory.createRawRuleRequestPacket(300, SINK_ADDR, 10));
        actPacket = deviceTxQ.take();

        expPacket = factory.createRawOpenPathPacket(SINK_ADDR,
                                                    300,
                                                    Arrays.asList(SINK_ADDR, 30, 35, 300));

        assertEquals(expPacket, actPacket);
    }

    /*
        Now let node 300 sends a new report and states it has
        no connection any more with node 35.
     */
    @Test
    public void if_a_link_is_dropped_it_should_find_another_path() throws InterruptedException
    {
        //drop link to node 35
        deviceRxQ.add(factory.createRawReportPacket(300,
                                                    SINK_ADDR,
                                                    factory.createNeighbors(36, 37)));

        deviceRxQ.add(factory.createRawRuleRequestPacket(300, SINK_ADDR, 10));
        actPacket = deviceTxQ.take();

        //new path should contains node 36
        expPacket = factory.createRawOpenPathPacket(SINK_ADDR,
                                                    300,
                                                    Arrays.asList(SINK_ADDR, 30, 36, 300));

        assertEquals(expPacket, actPacket);
    }

    //TODO there is no way to change the weight in this integration test. Move it to unit test
//    @Test
//    public void it_should_choose_path_with_lowest_weight() throws InterruptedException
//    {
//        deviceRxQ.add(factory.createRawReportPacket(300, SINK_ADDR,
//                                                    Arrays.asList(
//                                                            0, 35, 100,
//                                                            0, 36, 100,
//                                                            0, 37, 50)));
//
//        deviceRxQ.add(factory.createRawRuleRequestPacket(300, SINK_ADDR, 10));
//        actPacket = deviceTxQ.take();
//
//        expPacket = factory.createRawOpenPathPacket(SINK_ADDR,
//                                                    300,
//                                                    Arrays.asList(SINK_ADDR, 30, 35, 300));
//
//        assertEquals(expPacket, actPacket);
//    }

}

class MockDeviceConnection implements DeviceConnection
{

    BlockingQueue<List<Integer>> rxQueue = new LinkedBlockingQueue<>();
    BlockingQueue<List<Integer>> txQueue = new LinkedBlockingQueue<>();

    @Override
    public void connect() throws ChaparConnectionException
    {

    }

    @Override
    public void start()
    {

    }

    @Override
    public void stop()
    {

    }

    public BlockingQueue<List<Integer>> getChaparTxQueue()
    {
        return txQueue;
    }

    public BlockingQueue<List<Integer>> getChaparRxQueue()
    {
        return rxQueue;
    }
}
