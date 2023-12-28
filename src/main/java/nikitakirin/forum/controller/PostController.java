package nikitakirin.forum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nikitakirin.forum.dto.PostDTO;
import nikitakirin.forum.entity.Post;
import nikitakirin.forum.repository.PostRepository;
import org.springframework.http.HttpStatus;
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
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    @GetMapping
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("posts", postRepository.findAll());
        model.addAttribute("request", request);
        return "post/index";
    }

    @GetMapping("{postId}")
    public String show(@PathVariable Long postId, Model model) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "post/show";
    }

    @GetMapping("save")
    public String save(PostDTO postDTO, Model model) {
        return "post/form";
    }

    @GetMapping("{postId}/edit")
    public String edit(@PathVariable Long postId, Model model) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            model.addAttribute("postDTO", post.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "post/form";
    }

    @PostMapping("save")
    public String save(@Valid PostDTO postDTO, BindingResult result, Model model,
                       RedirectAttributes redirectAttributes) {
        Map<String, String> messages = new HashMap<>();
        if (result.hasErrors()) {
            messages.put("danger", "You entered incorrect data!");
            model.addAttribute("messages", messages);
            return save(postDTO, model);
        }
        Post post = postDTO.toEntity();
        postRepository.save(post);
        messages.put("success", "Post successfully saved!");
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("{postId}/delete")
    public String delete(@PathVariable Long postId, RedirectAttributes redirectAttributes) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        postRepository.deleteById(postId);
        Map<String, String> messages = new HashMap<>();
        messages.put("success", "Post successfully deleted!");
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/posts";
    }
}
