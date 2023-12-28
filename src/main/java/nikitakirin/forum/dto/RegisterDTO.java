package nikitakirin.forum.dto;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import nikitakirin.forum.entity.User;

@Data
public class RegisterDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public User toEntity() {
        User user = new User();
        user.setUsername(username);
        user.setAuthority("USER");
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        return user;
    }
}
