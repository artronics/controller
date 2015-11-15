package artronics.gsdwn.packet;

import java.util.List;

public interface Packet
{
    Integer getSource();

    List<Integer> getContent();

    SdwnPacketType getType();

}
