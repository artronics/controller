package artronics.gsdwn.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sdwn_controller")
public class ControllerConfig
{
    private String ip;

    private Date created;

    private Date updated;

    public ControllerConfig()
    {
    }

    public ControllerConfig(String ip)
    {
        this.ip = ip;
    }

    //Do not add generator. By default it is ASSIGNED, means it is assigned by app
    //TODO add validation
    @Id
    @Column(name = "controller_ip", nullable = false, unique = true)
    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    @PrePersist
    protected void onCreate()
    {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate()
    {
        updated = new Date();
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

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated)
    {
        this.updated = updated;
    }
}
