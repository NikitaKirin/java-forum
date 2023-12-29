package nikitakirin.forum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nikitakirin.forum.dto.CategoryDTO;
import nikitakirin.forum.dto.CommentDTO;
import nikitakirin.forum.entity.Category;
import nikitakirin.forum.entity.Comment;
import nikitakirin.forum.entity.Post;
import nikitakirin.forum.entity.User;
import nikitakirin.forum.repository.CommentRepository;
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
@RequestMapping("comments")
@RequiredArgsConstructor
public class CommentController {

    private final UserService userService;

    private final CommentRepository commentRepository;

    @PostMapping("save")
    public String save(@Valid CommentDTO commentDTO, BindingResult result, Model model,
                       RedirectAttributes redirectAttributes, Authentication authentication) {
        Map<String, String> messages = new HashMap<>();
        if (result.hasErrors()) {
            messages.put("danger", "You entered incorrect comment!");
            redirectAttributes.addFlashAttribute("messages", messages);
            return "redirect:/posts/" + commentDTO.getPost().getId();
        }
        var userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        Comment comment = commentDTO.toEntity();
        comment.setUser(user);
        commentRepository.save(comment);
        messages.put("success", "Comment successfully saved!");
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/posts/" + commentDTO.getPost().getId();
    }

    @GetMapping("{commentId}/delete")
    public String delete(@PathVariable Long commentId, RedirectAttributes redirectAttributes) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Long postId = comment.get().getPost().getId();
        commentRepository.deleteById(commentId);
        Map<String, String> messages = new HashMap<>();
        messages.put("success", "Comment successfully deleted!");
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/posts/" + postId;
    }
}
