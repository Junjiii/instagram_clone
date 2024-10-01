package instagram.instagram.web.dto.post;


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

    public PostCommentDto(Long commentId, String memberProfileImageUrl, String memberNickname, String comment) {
        this.commentId = commentId;
        this.memberProfileImageUrl = memberProfileImageUrl;
        this.memberNickname = memberNickname;
        this.comment = comment;
    }

}
