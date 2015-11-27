package artronics.gsdwn.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sdwn_controller")
public class ControllerConfig
{
    private Long id;

    private String ip;

    private Date created;

    private Date updated;

//    private Set<Session> sessions;

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

//    @OneToMany(mappedBy = "controllerEntity")
//    public Set<Session> getSessions()
//    {
//        return sessions;
//    }

//    public void setSessions(Set<Session> sessions)
//    {
//        this.sessions = sessions;
//    }

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
