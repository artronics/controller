package artronics.gsdwn.node;

public class Neighbor extends SdwnNode
{
    private final int rssi;

    public Neighbor(Integer address, int rssi)
    {
        super(address);
        this.rssi = rssi;
    }

    public int getRssi()
    {
        return rssi;
    }
}
