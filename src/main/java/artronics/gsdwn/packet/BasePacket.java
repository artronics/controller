package artronics.gsdwn.packet;

import java.util.List;

public class BasePacket extends PacketPojo implements Packet
{
    private final List<Integer> content;
//    private final List<Integer> header;
//    private final List<Integer> payload;

    public BasePacket(List<Integer> content)
    {
        this.content = content;
//        this.header = new ArrayList<>(content.subList(0,10));
//        this.payload = new ArrayList<>(content.subList(10,content.size()));
    }

    @Override
    public List<Integer> getContent()
    {
        return content;
    }

    @Override
    public Integer getSource()
    {
        return SdwnPacketHelper.getSourceAddress(content);
    }

    @Override
    public SdwnPacketType getType()
    {
        return SdwnPacketHelper.getType(content);
    }
}
