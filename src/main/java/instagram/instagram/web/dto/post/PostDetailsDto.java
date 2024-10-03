package instagram.instagram.web.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import instagram.instagram.domain.post.entity.Post;
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
    private MemberDto member;
    private List<PostHashtagDto> postHashtags;
    private List<PostDetailsImageDto> postDetailsImages;
    private List<PostCommentDto> postComments;
    private int postLikes;

    @QueryProjection
    public PostDetailsDto(Post post) {
        this.postId = post.getId();
        this.content = post.getContent();
        this.postLikes = post.getPostLikes().size();
        this.member = new MemberDto(post.getMember().getNickname(),post.getMember().getProfileImage());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class MemberDto {
        private String nickname;
        private String profileImage;

        @QueryProjection
        public MemberDto(String nickname, String profileImage) {
            this.nickname = nickname;
            this.profileImage = profileImage;
        }
    }
}
