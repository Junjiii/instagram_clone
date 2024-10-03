package instagram.instagram.web.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberProfileDto {

    private Long memberId;
    private String nickname;
    private String profileImage;
    private String bio;
    private List<MemberProfilePostDto> posts; // Post 정보를 리스트로 받음
    private int followings;
    private int followers;


    @QueryProjection
    public MemberProfileDto(Long memberId, String nickname, String profileImage, String bio, Long followings, Long followers) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.bio = bio;
        this.followings = followings.intValue();
        this.followers = followers.intValue();
    }


}

