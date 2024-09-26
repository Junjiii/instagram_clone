package instagram.instagram.web.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import instagram.instagram.domain.member.Gender;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.post.Post;
import instagram.instagram.domain.post.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MemberProfileDto {
        private Long memberId;
        private String email;
        private String password;
        private String name;
        private String phoneNumber;
        private Gender gender;
        private LocalDate birth;
        private String nickname;
        private String profileImage;
        private String bio;
        private List<MemberPostDto>  posts;
        private int followers;
        private int followings;

        @QueryProjection
        public MemberProfileDto(Member member) {
            this.memberId = member.getId();
            this.email = member.getEmail();
            this.password = member.getPassword();
            this.name = member.getName();
            this.phoneNumber = member.getPhoneNumber();
            this.gender = member.getGender();
            this.birth = member.getBirth();
            this.nickname = member.getNickname();
            this.profileImage = member.getProfileImage();
            this.bio = member.getBio();
            this.posts = member.getPosts().stream().sorted(Comparator.comparing(Post::getCreatedDate).reversed()).map(MemberPostDto::new).collect(Collectors.toList());
            this.followers = member.getFollowers().size();
            this.followings = member.getFollowings().size();
        }
}

@Getter
@NoArgsConstructor
class MemberPostDto {
    private Long postId;
    private String content;
    private PostImagesDto postImage;

    @QueryProjection
    public MemberPostDto(Post post) {
        this.postId = post.getId();
        this.content = post.getContent();
        this.postImage = post.getPostImages().stream().filter(postImage -> postImage.getSequence() == 1).findFirst().map(PostImagesDto::new).get();
    }
}

@Getter
@NoArgsConstructor
class PostImagesDto {
    private String imageUrl;

    @QueryProjection
    public PostImagesDto(PostImage postImage) {
        this.imageUrl = postImage.getImage_URL();
    }
}


