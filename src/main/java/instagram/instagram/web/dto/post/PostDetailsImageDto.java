package instagram.instagram.web.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import instagram.instagram.domain.post.entity.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDetailsImageDto {
    private String imageUrl;

    @QueryProjection
    public PostDetailsImageDto(PostImage postImage) {
        this.imageUrl = postImage.getImage_URL();
    }
}
