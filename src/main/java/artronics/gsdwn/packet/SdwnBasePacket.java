package artronics.gsdwn.packet;

import artronics.gsdwn.log.Log;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "packets")
public class SdwnBasePacket implements Packet
{
    private static long sequence = 0;
    protected List<Integer> content;
    protected Integer srcShortAddress;
    protected Integer dstShortAddress;
    protected Integer netId;
    protected Integer ttl;
    protected Integer nextHop;
    private Long id;
    private Timestamp receivedAt;

    private Long sessionId;
    private String controllerIp;

    /**
     * Never call this constructor. It is for Jpa entity.
     */
    public SdwnBasePacket()
    {
    }

    public SdwnBasePacket(List<Integer> content)
    {
        this.content = content;

        this.srcShortAddress = SdwnPacketHelper.getSourceAddress(content);
        this.dstShortAddress = SdwnPacketHelper.getDestinationAddress(content);

        this.netId = content.get(SdwnPacket.ByteMeaning.NET_ID.getValue());
        this.ttl = content.get(SdwnPacket.ByteMeaning.TTL.getValue());

        this.nextHop = SdwnPacketHelper.joinAddresses(
                content.get(SdwnPacket.ByteMeaning.NEXT_HOP_H.getValue()),
                content.get(SdwnPacket.ByteMeaning.NEXT_HOP_L.getValue())
        );

        sequence++;
        this.receivedAt = new Timestamp(new Date().getTime());

        Log.PACKET.debug(toString());
    }

    @Column(name = "sequence", nullable = false)
    public static Long getSequence()
    {
        return sequence;
    }

    public static void setSequence(Long sequence)
    {
        SdwnBasePacket.sequence = sequence;
    }

    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "controller_ip", nullable = false)
    public String getControllerIp()
    {
        return controllerIp;
    }

    public void setControllerIp(String controllerIp)
    {
        this.controllerIp = controllerIp;
    }

    @Column(name = "session_id", nullable = false)
    public Long getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(Long session_id)
    {
        this.sessionId = session_id;
    }

    @Override
    @Column(name = "src_short_add")
    public Integer getSrcShortAddress()
    {
        return srcShortAddress;
    }

    @Column(name = "dst_short_add")
    public Integer getDstShortAddress()
    {
        return dstShortAddress;
    }

    @Override
    @ElementCollection
//    @CollectionType(type = "java.util.ArrayList")
    @CollectionTable(name = "packet_content", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "content")
    public List<Integer> getContent()
    {
        return content;
    }

    public void setContent(List<Integer> content)
    {
        this.content = content;
    }

    public void setSrcShortAddress(Integer srcShortAddress)
    {
        this.srcShortAddress = srcShortAddress;
    }

    public void setDstShortAddress(Integer dstShortAddress)
    {
        this.dstShortAddress = dstShortAddress;
    }

    @Column(name = "received_at", nullable = false)
    public Timestamp getReceivedAt()
    {
        return receivedAt;
    }

    public void setReceivedAt(Timestamp receivedAt)
    {
        this.receivedAt = receivedAt;
    }

    @Column(name = "net_id")
    public Integer getNetId()
    {
        return netId;
    }

    public void setNetId(Integer netId)
    {
        this.netId = netId;
    }

    @Column(name = "ttl")
    public Integer getTtl()
    {
        return ttl;
    }

    public void setTtl(Integer ttl)
    {
        this.ttl = ttl;
    }

    @Column(name = "next_hop")
    public Integer getNextHop()
    {
        return nextHop;
    }

    public void setNextHop(Integer nextHop)
    {
        this.nextHop = nextHop;
    }

    @Override
    @Transient
    public SdwnPacketType getType()
    {
        return SdwnPacketHelper.getType(content);
    }

    @Override
    public String toString()
    {
        String s = "";
        s += String.format("%-6s", getType().toString());
        s += printContent(getContent());

        return s;
    }

    private String printContent(List<Integer> content)
    {
        String s = "";
        for (Integer i : content) {
            s += String.format("%-3d", i);
            s += " ,";
        }

        return s;
    }
}
