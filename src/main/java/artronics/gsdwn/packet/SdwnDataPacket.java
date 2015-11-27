package artronics.gsdwn.packet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static artronics.gsdwn.packet.SdwnPacketType.DATA;

@Entity
@Table(name = "packets")
@DiscriminatorValue("data")
public class SdwnDataPacket extends SdwnBasePacket
{
    public final static int HEADER_INDEX = 10;

    private SdwnDataPacket(List<Integer> content)
    {
        super(content);
    }

    public static SdwnDataPacket create(List<Integer> content)
    {
        return new SdwnDataPacket(content);
    }

    public static SdwnDataPacket create(int src, int dst, List<Integer> payload)
    {
        List<Integer> content = new ArrayList<>(
                SdwnPacketHeader.create(HEADER_INDEX + payload.size(), DATA, src, dst));

        content.addAll(payload);

        return new SdwnDataPacket(content);
    }

    public static SdwnDataPacket create(int src, int dst, int payloadLen)
    {
        return create(src, dst, createDummyPayload(payloadLen));
    }

    public static List<Integer> createDummyPayload(int len)
    {
        List<Integer> payload = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            payload.add(i);
        }

        return payload;
    }
}
