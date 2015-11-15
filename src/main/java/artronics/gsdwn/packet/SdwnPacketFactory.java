package artronics.gsdwn.packet;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SdwnPacketFactory implements PacketFactory
{
    @Override
    public Packet create(List<Integer> packetContent)
    {
        Packet packet = null;

        switch (SdwnPacketHelper.getType(packetContent)) {
            case REPORT:
                packet = new SdwnReportPacket(packetContent);
                break;

        }

        return packet;
    }
}
