package artronics.gsdwn.packet;

import java.util.List;

public class SdwnReportPacket extends SdwnBasePacket
{
    public static final int NEIGHBOR_INDEX = 13;
    private int battery;

    public SdwnReportPacket(List<Integer> content)
    {
        super(content);
        battery = content.get(SdwnPacket.ByteMeaning.BATTERY.getValue());
    }

    public int getBattery()
    {
        return battery;
    }
}
