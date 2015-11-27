package artronics.gsdwn.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "sdwn_controller")
public class ControllerConfig
{
    private Long id;

    private String ip;

    private Date created;

    private Date updated;

    private Set<ControllerSession> controllerSessions;

    public ControllerConfig()
    {
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

    @OneToMany(mappedBy = "controllerConfig")
    public Set<ControllerSession> getControllerSessions()
    {
        return controllerSessions;
    }

    public void setControllerSessions(Set<ControllerSession> controllerSessions)
    {
        this.controllerSessions = controllerSessions;
    }

    //TODO add validation
    @Column(name = "controller_ip", unique = false, nullable = false)
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
