package artronics.gsdwn.integration;

import artronics.chaparMini.DeviceConnection;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.controller.SdwnController;
import artronics.gsdwn.helper.FakePacketFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerTest
{
    DeviceConnection fakeDevCon = new FakeDeviceConn();

    Controller controller = new SdwnController(fakeDevCon);

    FakePacketFactory factory = new FakePacketFactory();

    @Before
    public void setUp() throws Exception
    {
        controller.start();

        controller.processPacket(factory.createReportPacket());
    }


    @Test
    public void it_should_send_rule_response_for_sink_node()
    {


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
