package nikitakirin.forum.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nikitakirin.forum.entity.Post;
import org.hibernate.validator.constraints.Length;

@Data
public class PostDTO {
    private Long id;

    @NotBlank
    @Length(max = 255)
    private String title;

    @NotBlank
    private String description;

    public Post toEntity() {
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setDescription(description);
        post.setCreationDatetime(System.currentTimeMillis());
        return post;
    }
}
