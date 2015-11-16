package artronics.gsdwn.packet;

import java.util.List;

public interface Packet
{
    Integer getSrcShortAddress();

    List<Integer> getContent();

    SdwnPacketType getType();

}
