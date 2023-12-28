package nikitakirin.forum.controller;

import lombok.RequiredArgsConstructor;
import nikitakirin.forum.dto.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("login")
public class LoginController {

    @GetMapping
    public String form(LoginDTO loginDTO, Model model) {
        return "auth/login";
    }
}
