package instagram.instagram.web.dto.post;

import com.querydsl.core.annotations.QueryProjection;
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
    public PostDetailsDto(List<PostFindDb> post) {
        this.postId = post.get(0).getPostId();
        this.content = post.get(0).getContent();
        this.member = new MemberDto(post.get(0).getNickname(),post.get(0).getProfileImage());
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
