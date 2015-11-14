package artronics.gsdwn.node;

public class SdwnNode implements Node
{
    /*
        In our node model address represent a 32 bit integer
        value which MUST represent this node with in this controller
        unique. If you want to use Extended Address you should create
        another class and implement node interface.
     */
    private final Integer address;

    public SdwnNode(Integer address)
    {
        this.address = address;
    }

    @Override
    public Integer getAddress()
    {
        return address;
    }

    @Override
    public int hashCode()
    {
        return getAddress().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Node) {
            Node node = (Node) obj;
            return getAddress().equals(node.getAddress());
        }

        return false;
    }

    @Override
    public String toString()
    {
        return getAddress().toString();
    }
}
