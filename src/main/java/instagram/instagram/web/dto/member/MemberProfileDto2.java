package instagram.instagram.web.dto.member;

import instagram.instagram.domain.member.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileDto2 {

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
    private List<PostDto> posts; // Post 정보를 리스트로 받음
    private int followers;       // fromMemberCount에 대응
    private int followings;      // toMemberCount에 대응









    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDto {
        private Long postId;
        private String content;
        private List<PostImageDto> postImage;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostImageDto {
        private String imageUrl;
    }
}

