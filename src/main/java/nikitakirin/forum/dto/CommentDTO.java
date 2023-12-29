package nikitakirin.forum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import nikitakirin.forum.entity.Comment;
import nikitakirin.forum.entity.Post;

@Data
public class CommentDTO {
    private Long id;

    @NotBlank
    private String text;

    private Post post;

    public Comment toEntity() {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setPost(post);
        return comment;
    }
}
