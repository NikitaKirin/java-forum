package nikitakirin.forum.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private Long creationDatetime;

}
