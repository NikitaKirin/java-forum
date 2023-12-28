package nikitakirin.forum.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        Map<String, String> messages = new HashMap<>();
        messages.put("info", "You're welcome!");
        model.addAttribute("request", request);
        model.addAttribute("messages", messages);
        return "home";
    }
}
