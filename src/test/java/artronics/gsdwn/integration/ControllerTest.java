package artronics.gsdwn.integration;

import artronics.chaparMini.DeviceConnection;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.controller.SdwnController;
import artronics.gsdwn.helper.FakePacketFactory;
import artronics.gsdwn.networkMap.*;
import artronics.gsdwn.packet.Packet;
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

    DeviceConnection fakeDevCon = new FakeDeviceConn();
    WeightCalculator weightCalculator = new FixedWeightCalculator(100);
    NetworkMap networkMap = new SdwnNetworkMap();
    NetworkMapUpdater mapUpdater = new NetworkMapUpdater(networkMap, weightCalculator);
    Controller controller = new SdwnController(fakeDevCon, networkMap, mapUpdater);

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

        deviceRxQ = fakeDevCon.getRxQueue();
        deviceTxQ = fakeDevCon.getTxQueue();

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
        // 10 is the data length. it doesn't matter
        //Since this process is just related to DeviceConnection
        //at this point we assume that sink already has received our
        //data packet

        //give it some time so mapUpdater can construct the graph
        Thread.sleep(400);

        //Sink will response with RuleRequest
        deviceRxQ.add(factory.createRawRuleRequestPacket(SINK_ADDR, 300, 10));
    }


    /*
        Now Controller should send OpenPath packet in response to RuleReq
     */
    @Test
    public void it_should_send_OpenPath_for_sink_node() throws InterruptedException
    {
        actPacket = deviceTxQ.take();
        Thread.sleep(400);
        expPacket = factory.createRawOpenPathPacket(SINK_ADDR, 300, Arrays.asList(30, 36));

        assertEquals(expPacket, actPacket);

    }

}

class FakeDeviceConn implements DeviceConnection
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

    @Override
    public BlockingQueue<List<Integer>> getTxQueue()
    {
        return txQueue;
    }

    @Override
    public BlockingQueue<List<Integer>> getRxQueue()
    {
        return rxQueue;
    }
}
