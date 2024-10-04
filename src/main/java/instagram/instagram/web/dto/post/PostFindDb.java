package instagram.instagram.web.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostFindDb {
        private Long postId;
        private String content;
        private String nickname;
        private String profileImage;
        private String postImage;
        private int postLikes;


        @QueryProjection
        public PostFindDb(Long postId, String content, String nickname, String profileImage, String postImage) {
                this.postId = postId;
                this.content = content;
                this.nickname = nickname;
                this.profileImage = profileImage;
                this.postImage = postImage;
        }
}
