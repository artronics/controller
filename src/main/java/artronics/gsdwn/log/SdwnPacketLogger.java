package artronics.gsdwn.log;

import artronics.chaparMini.PacketLogger;
import artronics.gsdwn.packet.SdwnPacketHelper;

import java.util.List;

public class SdwnPacketLogger implements PacketLogger
{
    @Override
    public String logPacket(List<Integer> packetContent)
    {
        String s = "";
        s += String.format("%-6s", SdwnPacketHelper.getType(packetContent).toString());
        s += ": ";

        for (int data : packetContent) {
            s += String.format("%-3d", data);
            s += " ,";
        }
        return s;
    }
}
