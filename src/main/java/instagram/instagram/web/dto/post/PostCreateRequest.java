package instagram.instagram.web.dto.post;

import lombok.Getter;

import java.util.List;

@Getter
public class PostCreateRequest {

    private String content;
    private List<String> imageUrls;
    private List<String> hashtags;


    public PostCreateRequest(String content, List<String> imageUrls, List<String> hashtags) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.hashtags = hashtags;
    }
}
