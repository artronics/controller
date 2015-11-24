package artronics.gsdwn.packet;

public enum SdwnPacketType
{
    DATA(0),
    MALFORMED(1),
    REPORT(2),
    BEACON(3),
    RL_REQ(128),
    RL_RES(4),
    OPN_PT(5);

    private int value;

    SdwnPacketType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
