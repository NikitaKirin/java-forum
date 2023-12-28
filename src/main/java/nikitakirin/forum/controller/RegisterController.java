package nikitakirin.forum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nikitakirin.forum.dto.RegisterDTO;
import nikitakirin.forum.entity.User;
import nikitakirin.forum.repository.UserRepository;
import nikitakirin.forum.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("register")
public class RegisterController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public String form(RegisterDTO registerDTO, Model model) {
        return "auth/register";
    }

    @PostMapping
    public String register(@Valid RegisterDTO registerDTO, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        Map<String, String> messages = new HashMap<>();
        if (result.hasErrors()) {
            messages.put("danger", "You entered incorrect data!");
            model.addAttribute("messages", messages);
            return form(registerDTO, model);
        }
        User user = registerDTO.toEntity();
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userRepository.save(user);
        messages.put("success", "You have successfully registered!");
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/";
    }
}
