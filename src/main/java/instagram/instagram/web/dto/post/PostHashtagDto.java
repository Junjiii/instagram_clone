package instagram.instagram.web.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostHashtagDto {
    private String hashtag;

    @QueryProjection
    public PostHashtagDto(String hashtag) {
        this.hashtag = hashtag;
    }
}
