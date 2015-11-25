package artronics.gsdwn.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "session")
public class Session
{
    private Long id;

    private Set<ControllerEntity> controllerEntities;
    private ControllerEntity controllerEntity;

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
    @JoinColumn(name = "controller_id")
    public ControllerEntity getControllerEntity()
    {
        return controllerEntity;
    }

    public void setControllerEntity(ControllerEntity controllerEntity)
    {
        this.controllerEntity = controllerEntity;
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
