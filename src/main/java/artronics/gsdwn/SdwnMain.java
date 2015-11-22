package artronics.gsdwn;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.SdwnController;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.SdwnDataPacket;

import java.util.concurrent.BlockingQueue;

public class SdwnMain
{
    public static void main(String[] args) throws InterruptedException
    {

        SdwnController controller = new SdwnController(new Chapar());

        try {

            controller.start();

        }catch (ChaparConnectionException e) {
            e.printStackTrace();
        }

        Thread.sleep(10000);

        SdwnDataPacket packet = SdwnDataPacket.create(0, 30, 10);
        BlockingQueue<Packet> queue = controller.getCntTxPackets();
        queue.add(packet);
    }
}
