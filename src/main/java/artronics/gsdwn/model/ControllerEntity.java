package artronics.gsdwn.model;

import javax.persistence.*;

@Entity
@Table(name = "sdwn_controller")
public class ControllerEntity
{
    private Long id;

    private String ip;

//    private Set<Session> sessions;

    public ControllerEntity()
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
}
