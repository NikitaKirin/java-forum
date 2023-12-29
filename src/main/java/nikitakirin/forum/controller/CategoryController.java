package nikitakirin.forum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nikitakirin.forum.dto.CategoryDTO;
import nikitakirin.forum.dto.PostDTO;
import nikitakirin.forum.entity.Category;
import nikitakirin.forum.entity.Post;
import nikitakirin.forum.entity.User;
import nikitakirin.forum.repository.CategoryRepository;
import nikitakirin.forum.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("request", request);
        return "category/index";
    }

    @GetMapping("save")
    public String save(CategoryDTO categoryDTO, Model model) {
        return "category/form";
    }

    @GetMapping("{categoryId}/edit")
    public String edit(@PathVariable Long categoryId, Model model) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            model.addAttribute("categoryDTO", category.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "category/form";
    }

    @PostMapping("save")
    public String save(@Valid CategoryDTO categoryDTO, BindingResult result, Model model,
                       RedirectAttributes redirectAttributes, Authentication authentication) {
        Map<String, String> messages = new HashMap<>();
        if (result.hasErrors()) {
            messages.put("danger", "You entered incorrect data!");
            model.addAttribute("messages", messages);
            return save(categoryDTO, model);
        }
        Category category = categoryDTO.toEntity();
        categoryRepository.save(category);
        messages.put("success", "Category successfully saved!");
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/categories";
    }

    @GetMapping("{categoryId}/delete")
    public String delete(@PathVariable Long categoryId, RedirectAttributes redirectAttributes) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(categoryId);
        Map<String, String> messages = new HashMap<>();
        messages.put("success", "Category successfully deleted!");
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/categories";
    }
}
