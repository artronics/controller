package artronics.gsdwn;

import artronics.chaparMini.Chapar;
import artronics.chaparMini.DeviceConnection;
import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.controller.SdwnController;
import artronics.gsdwn.log.SdwnPacketLogger;
import artronics.gsdwn.networkMap.NetworkMap;
import artronics.gsdwn.networkMap.NetworkMapUpdater;
import artronics.gsdwn.networkMap.RssiSimpleWeightCalculator;
import artronics.gsdwn.networkMap.SdwnNetworkMap;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.SdwnDataPacket;

import java.util.concurrent.BlockingQueue;

public class SdwnMain
{
    public static void main(String[] args) throws InterruptedException
    {
        DeviceConnection chapar = new Chapar(new SdwnPacketLogger());
        NetworkMap networkMap = new SdwnNetworkMap();
        NetworkMapUpdater mapUpdater = new NetworkMapUpdater(networkMap, new
                RssiSimpleWeightCalculator());
        Controller controller = new SdwnController(chapar, networkMap, mapUpdater);

        try {

            controller.start();

        }catch (ChaparConnectionException e) {
            e.printStackTrace();
        }

        Thread.sleep(10000);

        SdwnDataPacket packet = SdwnDataPacket.create(0, 30, 10);
        BlockingQueue<Packet> queue = controller.getCntTxPacketsQueue();
        queue.add(packet);

        Thread.sleep(10000);

        for (int i = 0; i < 1000; i++) {
            SdwnDataPacket dataPacket = SdwnDataPacket.create(0, 30, 10);
            queue.add(dataPacket);
            Thread.sleep(1000);
        }
    }
}
