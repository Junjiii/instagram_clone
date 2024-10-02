package instagram.instagram.web.dto.post;

import instagram.instagram.domain.post.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDetailsImageDto {
    private String imageUrl;

    public PostDetailsImageDto(PostImage postImage) {
        this.imageUrl = postImage.getImage_URL();
    }
}
