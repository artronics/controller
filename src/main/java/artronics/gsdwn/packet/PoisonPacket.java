package artronics.gsdwn.packet;

import java.util.List;

/**
 * This class will use only as POISON_PILL Sending this packet to a queue stops the process (thread)
 * from running.
 */
public class PoisonPacket implements Packet
{
    @Override
    public Integer getSource()
    {
        return null;
    }

    @Override
    public List<Integer> getContent()
    {
        return null;
    }

    @Override
    public SdwnPacketType getType()
    {
        return null;
    }
}
