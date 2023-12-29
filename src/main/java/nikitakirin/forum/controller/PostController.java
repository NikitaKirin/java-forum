package nikitakirin.forum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nikitakirin.forum.dto.CommentDTO;
import nikitakirin.forum.dto.PostDTO;
import nikitakirin.forum.entity.Post;
import nikitakirin.forum.entity.User;
import nikitakirin.forum.repository.CategoryRepository;
import nikitakirin.forum.repository.PostRepository;
import nikitakirin.forum.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.print.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @GetMapping
    public String index(HttpServletRequest request, Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Post> posts = postRepository.findAll(PageRequest.of(page, 3));
        model.addAttribute("posts", posts);
        model.addAttribute("request", request);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "post/index";
    }

    @GetMapping("{postId}")
    public String show(@PathVariable Long postId, Model model, CommentDTO commentDTO) {
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
        model.addAttribute("categories", categoryRepository.findAll());
        return "post/form";
    }

    @GetMapping("{postId}/edit")
    public String edit(@PathVariable Long postId, Model model) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("postDTO", post.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "post/form";
    }

    @PostMapping("save")
    public String save(@Valid PostDTO postDTO, BindingResult result, Model model,
                       RedirectAttributes redirectAttributes, Authentication authentication) {
        Map<String, String> messages = new HashMap<>();
        if (result.hasErrors()) {
            messages.put("danger", "You entered incorrect data!");
            model.addAttribute("messages", messages);
            return save(postDTO, model);
        }
        Post post = postDTO.toEntity();
        var userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        post.setUser(user);
        post.setCategories(postDTO.getCategories());
        if (postDTO.getId() != null) {
            post.setComments(postRepository.findById(postDTO.getId()).get().getComments());
        }
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
