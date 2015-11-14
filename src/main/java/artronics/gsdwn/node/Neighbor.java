package artronics.gsdwn.node;

import artronics.gsdwn.packet.SdwnPacketHelper;
import artronics.gsdwn.packet.SdwnReportPacket;

import java.util.ArrayList;
import java.util.List;

public class Neighbor extends SdwnNode
{
    private final int rssi;

    public Neighbor(Integer address, int rssi)
    {
        super(address);
        this.rssi = rssi;
    }

    public static List<Neighbor> createNeighbors(SdwnReportPacket packet)
    {
        List<Neighbor> neighbors = new ArrayList<>();
        List<Integer> contents = packet.getContent();

        for (int i = SdwnReportPacket.NEIGHBOR_INDEX; i < contents.size(); i += 3) {
            int add = SdwnPacketHelper.joinAddresses(contents.get(i),
                                                     contents.get(i + 1));
            int rssi = contents.get(i + 2);
            Neighbor neighbor = new Neighbor(add, rssi);
            neighbors.add(neighbor);
        }
        return neighbors;
    }

    public int getRssi()
    {
        return rssi;
    }
}
