package artronics.gsdwn.packet;

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
}
