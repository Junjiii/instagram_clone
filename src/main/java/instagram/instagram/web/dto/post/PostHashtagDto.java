package instagram.instagram.web.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostHashtagDto {
    private String hashtag;

    public PostHashtagDto(String hashtag) {
        this.hashtag = hashtag;
    }
}
