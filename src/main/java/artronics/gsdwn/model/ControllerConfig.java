package artronics.gsdwn.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "sdwn_controller")
public class ControllerConfig
{
    private String ip;

    private Date created;

    private Date updated;

    private Set<ControllerSession> controllerSessions;

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

    @OneToMany(mappedBy = "controllerConfig")
    public Set<ControllerSession> getControllerSessions()
    {
        return controllerSessions;
    }

    public void setControllerSessions(Set<ControllerSession> controllerSessions)
    {
        this.controllerSessions = controllerSessions;
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
