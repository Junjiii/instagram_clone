package instagram.instagram.web.dto.member;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberProfilePostDto {

    private Long postId;
    private String postImage;

    @QueryProjection
    public MemberProfilePostDto(Long postId, String postImage) {
        this.postId = postId;
        this.postImage = postImage;
    }
}
