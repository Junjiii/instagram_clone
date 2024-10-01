package instagram.instagram.web.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDetailsImageDto {
    private String imageUrl;

    public PostDetailsImageDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
