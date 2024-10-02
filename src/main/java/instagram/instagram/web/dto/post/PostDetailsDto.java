package instagram.instagram.web.dto.post;

import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

        public MemberDto(String nickname, String profileImage) {
            this.nickname = nickname;
            this.profileImage = profileImage;
        }
    }
}
