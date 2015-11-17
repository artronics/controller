package artronics.gsdwn.helper;

import artronics.gsdwn.packet.SdwnPacket;
import artronics.gsdwn.packet.SdwnPacketHelper;
import artronics.gsdwn.packet.SdwnPacketType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * It creates a fake SdwnPacket of all types The default convention for non-parameter methods is as
 * follow: src is always 30, dst is always 0, payload for data is from 0 to payload length and for
 * default length is 10,
 */
public class FakePacketFactory
{
    List<Integer> packet = new ArrayList<>();
    List<Integer> header = new ArrayList<>();

    private List<Integer> createHeader()
    {
        return createHeader(10, SdwnPacketType.DATA, 30, 0);
    }

    private List<Integer> createHeader(int len, SdwnPacketType type, int src, int dst)
    {
        Integer[] bytes = {
                len,//length
                1, //NetId
                SdwnPacketHelper.getHighAddress(src),
                SdwnPacketHelper.getLowAddress(src),

                SdwnPacketHelper.getHighAddress(dst),
                SdwnPacketHelper.getLowAddress(dst),
                type.getValue(),//type
                20, //MAX_TTL
                0,//next hop H
                0,//next hop L
        };
        header = Arrays.asList(bytes);

        return header;
    }

    public List<Integer> createReportPacket(int src, int dst, int dis, int bat,
                                            List<Integer> neighbors)
    {
        List<Integer> header = createHeader(SdwnPacket.HEADER_INDEX + 3 + neighbors.size(),
                                            SdwnPacketType.REPORT,
                                            src, dst);

        List<Integer> extra = new ArrayList<>();
        extra.add(dis);//distance
        extra.add(bat);//battery
        extra.add(neighbors.size() / 3);//number of neighbors

        packet = new ArrayList<>(header);
        packet.addAll(extra);
        packet.addAll(neighbors);

        return packet;
    }

    public List<Integer> createReportPacket()
    {
        List<Integer> neighbors = createNeighbors(35, 36, 37);

        return createReportPacket(30, 0, 1, 255, neighbors);
    }

    public List<Integer> createNeighbors(int... addrs)
    {
        List<Integer> neighbors = new ArrayList<>();
        Random rn = new Random();

        for (int addr : addrs) {
            neighbors.add(SdwnPacketHelper.getHighAddress(addr));
            neighbors.add(SdwnPacketHelper.getLowAddress(addr));
            neighbors.add(rn.nextInt(255)); //random rssi
        }

        return neighbors;
    }

    public List<Integer> createDataPacket(int src, int dst, int payloadLen)
    {
        List<Integer> header = createHeader(SdwnPacket.HEADER_INDEX + payloadLen,
                                            SdwnPacketType.DATA, src, dst);

        List<Integer> packet = new ArrayList<>(header);
        for (int i = 0; i < payloadLen; i++) {
            packet.add(i);
        }

        return packet;
    }

    public List<Integer> createDataPacket()
    {
        return createDataPacket(30, 0, 10);
    }

}
