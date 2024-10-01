package instagram.instagram.web.controller;

import instagram.instagram.service.PostService;
import instagram.instagram.web.dto.post.PostDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{id}")
    public PostDetailsDto findPostDetails(@PathVariable("id") Long id) {
        return postService.findPostDetails(id);
    }
}
