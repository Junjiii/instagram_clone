package instagram.instagram.web.dto.post;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCommentDto {
    private Long commentId;
    private String memberProfileImageUrl;
    private String memberNickname;
    private String comment;

    @QueryProjection
    public PostCommentDto(Long commentId, String profileImage, String nickname, String comment ) {
        this.commentId = commentId;
        this.memberProfileImageUrl = profileImage;
        this.memberNickname = nickname;
        this.comment = comment;
    }

}
