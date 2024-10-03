package instagram.instagram.web.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostCreateRequest {

    private String content;
    private List<String> imageUrls;
    private List<String> hashtags;

    @QueryProjection
    public PostCreateRequest(String content, List<String> imageUrls, List<String> hashtags) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.hashtags = hashtags;
    }
}
