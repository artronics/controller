package artronics.gsdwn.model;

import javax.persistence.*;

@Entity
@Table(name = "sdwn_controller")
public class ControllerEntity
{
    private Long id;
    private String ip;

    public ControllerEntity(String ip)
    {
        this.ip = ip;
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
