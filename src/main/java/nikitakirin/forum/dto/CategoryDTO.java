package nikitakirin.forum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import nikitakirin.forum.entity.Category;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryDTO {
    private Long id;

    @NotBlank
    @Length(max = 255)
    private String title;

    public Category toEntity() {
        Category category = new Category();
        category.setTitle(title);
        return category;
    }
}
