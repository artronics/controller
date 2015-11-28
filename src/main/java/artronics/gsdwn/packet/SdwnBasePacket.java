package artronics.gsdwn.packet;

import artronics.gsdwn.log.Log;
import artronics.gsdwn.model.ControllerSession;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "packets")
public class SdwnBasePacket implements Packet
{
    protected List<Integer> content;
    protected Integer srcShortAddress;
    protected Integer dstShortAddress;
    private Long id;

    private ControllerSession controllerSession;

    @Transient
    private String timeStamp;

    public SdwnBasePacket()
    {
    }

    public SdwnBasePacket(List<Integer> content)
    {
        this.content = content;

        this.srcShortAddress = SdwnPacketHelper.getSourceAddress(content);
        this.dstShortAddress = SdwnPacketHelper.getDestinationAddress(content);

        Log.PACKET.debug(toString());
    }


    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    public Long getId()
    {
        return id;
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

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setSrcShortAddress(Integer srcShortAddress)
    {
        this.srcShortAddress = srcShortAddress;
    }

    public void setDstShortAddress(Integer dstShortAddress)
    {
        this.dstShortAddress = dstShortAddress;
    }

    @Override
    @Transient
    public List<Integer> getContent()
    {
        return content;
    }

    public void setContent(List<Integer> content)
    {
        this.content = content;
    }

    @Override
    @Transient
    public SdwnPacketType getType()
    {
        return SdwnPacketHelper.getType(content);
    }

    @ManyToOne
    @JoinColumn(name = "session_id")
    public ControllerSession getControllerSession()
    {
        return controllerSession;
    }

    public void setControllerSession(ControllerSession controllerSession)
    {
        this.controllerSession = controllerSession;
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
