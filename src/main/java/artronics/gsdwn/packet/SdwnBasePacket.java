package artronics.gsdwn.packet;

import artronics.gsdwn.log.Log;

import java.util.List;

public class SdwnBasePacket implements Packet
{
    protected final List<Integer> content;

    protected Integer srcShortAddress;

    protected Integer dstShortAddress;

    private String timeStamp;

    public SdwnBasePacket(List<Integer> content)
    {
        this.content = content;

        this.srcShortAddress = SdwnPacketHelper.getSourceAddress(content);
        this.dstShortAddress = SdwnPacketHelper.getDestinationAddress(content);

        Log.PACKET.debug(toString());
    }

    @Override
    public List<Integer> getContent()
    {
        return content;
    }

    @Override
    public Integer getSrcShortAddress()
    {
        return srcShortAddress;
    }

    @Override
    public SdwnPacketType getType()
    {
        return SdwnPacketHelper.getType(content);
    }

    public Integer getDstShortAddress()
    {
        return dstShortAddress;
    }

    @Override
    public String toString()
    {
        String s = "";
        s += String.format("%-13s", getType().toString());
        s += printContent(getContent());

        return s;
    }

    private String printContent(List<Integer> content)
    {
        String s = "";
        for (Integer i : content) {
            s += String.format("%-3d", i);
            s += " ,";
        }

        return s;
    }
}
