package artronics.gsdwn.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "session")
public class ControllerSession
{
    private Long id;

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
