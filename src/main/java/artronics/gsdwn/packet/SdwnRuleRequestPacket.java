package artronics.gsdwn.packet;

import java.util.List;

public class SdwnRuleRequestPacket extends SdwnBasePacket
{
    public SdwnRuleRequestPacket(List<Integer> content)
    {
        super(content);
    }
}
