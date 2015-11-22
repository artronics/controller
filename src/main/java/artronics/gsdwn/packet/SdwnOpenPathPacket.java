package artronics.gsdwn.packet;

import artronics.gsdwn.node.Node;

import java.util.ArrayList;
import java.util.List;

import static artronics.gsdwn.packet.SdwnPacketHelper.*;

public class SdwnOpenPathPacket extends SdwnBasePacket
{
    public final static int HEADER_INDEX = 10;

    private SdwnOpenPathPacket(List<Integer> content)
    {
        super(content);
    }


    /**
     * Create an Open path packet.
     *
     * @param nodes first node is source and last one is destination. Others are put inside payload
     *              as path from source towards destination.
     * @return
     */
    public static SdwnOpenPathPacket create(List<Node> nodes)
    {

        int len = HEADER_INDEX + 2 * nodes.size() - 4;
        int src = nodes.get(0).getAddress();
        int dst = nodes.get(nodes.size() - 1).getAddress();

        List<Integer> content = new ArrayList<>(
                SdwnPacketHeader.create(len, SdwnPacketType.OPEN_PATH, src, dst));

        nodes.remove(0);//remove src
        nodes.remove(nodes.size()-1);//remove dst

        List<Integer> payload = new ArrayList<>();
        int pathLength = nodes.size()*2;
        List<Integer> addrs = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i ++ ) {
            int addr = nodes.get(i).getAddress();
            payload.add(i*2,
                        getHighAddress(addr));
            payload.add(i*2+1,
                        getLowAddress(addr));
        }

        content.addAll(payload);

        return new SdwnOpenPathPacket(content);
    }

    public static List<Integer> getNodesAddresses(List<Integer> content)
    {
        List<Integer> addrs = new ArrayList<>();

        for (int i = HEADER_INDEX; i < content.size(); i += 2) {
            addrs.add(
                    joinAddresses(
                            content.get(i),
                            content.get(i+1)
                    ));
        }
        return addrs;
    }

}
