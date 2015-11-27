package artronics.gsdwn.model;

import javax.persistence.*;

@Entity
@Table(name = "session")
public class ControllerSession
{
    private Long id;

    private ControllerConfig controllerConfig;

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
    @JoinColumn(name = "controller_ip")
    public ControllerConfig getControllerConfig()
    {
        return controllerConfig;
    }

    public void setControllerConfig(ControllerConfig controllerConfig)
    {
        this.controllerConfig = controllerConfig;
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
