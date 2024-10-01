package instagram.instagram.web.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDetailsDto {
    private Long postId;
    private String content;
    private List<PostHashtagDto> postHashtags;
    private List<PostDetailsImageDto> postDetailsImages;
    private List<PostCommentDto> postComments;
    private int postLikes;

    public PostDetailsDto(Long postId, String content, int postLikes) {
        this.postId = postId;
        this.content = content;
        this.postLikes = postLikes;
    }
}
