package artronics.gsdwn.packet;

import java.util.ArrayList;
import java.util.List;


public final class SdwnPacketHelper
{
    public static boolean validate(List<Integer> receivedBytes)
    {
        final int length = getLength(receivedBytes);

        //Now place your validation rules here.
        return (length == receivedBytes.size());

    }

    public static int getLength(List<Integer> receivedBytes)
    {
        return receivedBytes.get(SdwnPacket.ByteMeaning.LENGTH.getValue());
    }

    public static SdwnPacketType getType(List<Integer> receviedBytes)
    {
        int typeIndex = receviedBytes.get(SdwnPacket.ByteMeaning.TYPE.getValue());
        for (SdwnPacketType type : SdwnPacketType.values()) {
            if (typeIndex == type.getValue())
                return type;
        }

        return SdwnPacketType.MALFORMED;
    }

    // gets two part of address and returns int address
    public static int getIntAddress(int sourceL, int sourceH)
    {
        int addr = sourceH;
        addr = (addr << 8) | sourceL;

        return addr;
    }

    @Deprecated
    public static SdwnPacketDirection chooseDirection(List<Integer> receivedBytes)
    {
        int sourceL = SdwnPacket.ByteMeaning.SOURCE_L.getValue();
        int sourceH = SdwnPacket.ByteMeaning.SOURCE_H.getValue();

        int sourceAddL = receivedBytes.get(sourceL);
        int sourceAddH = receivedBytes.get(sourceH);
        int source_addr = getIntAddress(sourceAddL, sourceAddH);

        //if source address is sink it is TX
        if (source_addr == SdwnPacket.SINK_ADDRESS) {
            return SdwnPacketDirection.TX;
        }

        return SdwnPacketDirection.RX;
    }

    public static int getSourceAddress(List<Integer> bytes)
    {
        int addH = bytes.get(SdwnPacket.ByteMeaning.SOURCE_H.getValue());
        int addL = bytes.get(SdwnPacket.ByteMeaning.SOURCE_L.getValue());
        return joinAddresses(addH, addL);
    }

    @Deprecated
    public static int[] splitAddress(int address)
    {
        int addH = (address & 0x0000FF00) >> 8;
        int addL = address & 0x000000FF;

        return new int[]{addH, addL};
    }

    public static int getHighAddress(int address)
    {
        int addH = (address & 0x0000FF00) >> 8;

        return addH;
    }

    public static int getLowAddress(int address)
    {
        int addL = address & 0x000000FF;

        return addL;
    }

    public static int joinAddresses(int add_H, int add_L)
    {
        return (add_H << 8) | add_L;
    }

    public static List<Integer> createDummyPayload(int size)
    {
        List<Integer> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(i);
        }

        return data;
    }

    public static int getDestinationAddress(List<Integer> bytes)
    {
        int addHIndex = SdwnPacket.ByteMeaning.DESTINATION_H.getValue();
        int addLIndex = SdwnPacket.ByteMeaning.DESTINATION_L.getValue();
        int addH = bytes.get(addHIndex);
        int addL = bytes.get(addLIndex);

        return joinAddresses(addH, addL);
    }

    public static int getNextHopAddress(List<Integer> bytes)
    {
        int addHIndex = SdwnPacket.ByteMeaning.NEXT_HOP_H.getValue();
        int addLIndex = SdwnPacket.ByteMeaning.NEXT_HOP_L.getValue();
        int addH = bytes.get(addHIndex);
        int addL = bytes.get(addLIndex);

        return joinAddresses(addH, addL);
    }
}
