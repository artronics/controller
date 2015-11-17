package artronics.gsdwn.packet;

import java.util.Arrays;
import java.util.List;

import static artronics.gsdwn.packet.SdwnPacket.ByteMeaning.*;
import static artronics.gsdwn.packet.SdwnPacketHelper.getHighAddress;
import static artronics.gsdwn.packet.SdwnPacketHelper.getLowAddress;

public class SdwnPacketHeader
{
    private final static int DF_NET_ID = 1;
    private final static int DF_NXT_HOP = 0;
    private final static int DF_SRC = 0;
    private final static int DF_DST = 30;
    private final static int DF_TYPE = 0;
    private final static int DF_TTL = 20;

    private final static int DF_PAYLAOD_LEN = 10;

    private SdwnPacketHeader()
    {
    }

    private static List<Integer> createDefaultHeader()
    {
        Integer[] header = {
                10,
                DF_NET_ID,

                getHighAddress(DF_SRC),
                getLowAddress(DF_SRC),

                getHighAddress(DF_DST),
                getLowAddress(DF_DST),

                DF_TYPE,
                DF_TTL,

                getHighAddress(DF_NXT_HOP),
                getLowAddress(DF_NXT_HOP),
        };
        return Arrays.asList(header);
    }

    public static List<Integer> create(int len, SdwnPacketType type, int src, int dst)
    {
        List<Integer> header = createDefaultHeader();

        header.set(LENGTH.getValue(), len);

        header.set(TYPE.getValue(), type.getValue());

        header.set(SOURCE_H.getValue(), getHighAddress(src));
        header.set(SOURCE_L.getValue(), getLowAddress(src));

        header.set(DESTINATION_H.getValue(), getHighAddress(dst));
        header.set(DESTINATION_L.getValue(), getLowAddress(dst));

        return header;
    }
}
