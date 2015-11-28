package artronics.gsdwn.packet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

@Entity
@DiscriminatorValue(value = "REPORT")
public class SdwnReportPacket extends SdwnBasePacket
{
    public static final int NEIGHBOR_INDEX = 13;
    private int battery;

    public SdwnReportPacket()
    {
    }

    public SdwnReportPacket(List<Integer> content)
    {
        super(content);
        battery = content.get(SdwnPacket.ByteMeaning.BATTERY.getValue());
    }

    @Transient
    public int getBattery()
    {
        return battery;
    }
}
