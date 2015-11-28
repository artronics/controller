package artronics.gsdwn.model;

import artronics.gsdwn.packet.SdwnBasePacket;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "session")
public class ControllerSession
{
    private Long id;

    private ControllerConfig controllerConfig;

    private List<SdwnBasePacket> packets;

    private Date created;

    private String description;


    @Id
    @GeneratedValue
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "controller_ip", nullable = false)
    public ControllerConfig getControllerConfig()
    {
        return controllerConfig;
    }

    public void setControllerConfig(ControllerConfig controllerConfig)
    {
        this.controllerConfig = controllerConfig;
    }

    @OneToMany(mappedBy = "controllerSession")
    public List<SdwnBasePacket> getPackets()
    {
        return packets;
    }

    public void setPackets(List<SdwnBasePacket> packets)
    {
        this.packets = packets;
    }

    @PrePersist
    protected void onCreate()
    {
        created = new Date();
    }

    @Column(name = "created", nullable = false)
    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }


    @Column(name = "description")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
