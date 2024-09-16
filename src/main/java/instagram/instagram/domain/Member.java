package instagram.instagram.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;
    private String nickname;
    private String profileImage;
    private String bio;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();


    @OneToMany(mappedBy = "toMember",cascade = CascadeType.ALL)
    private List<Follow> followers = new ArrayList<>();


    @OneToMany(mappedBy = "fromMember")
    private List<Follow> followings = new ArrayList<>();


    public Member( String email,String password, String name, String phoneNumber,Gender gender, LocalDate birth) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = name; // 최초 생성시 default 값 name 값 -> setNickname 으로 추후 변경 유도
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birth = birth;
    }


    public Member(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }



    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    //== 연관관계 메서드 ==//
    public void addFollowing(Member toMember) {
        Follow following = new Follow(this, toMember);
        this.followings.add(following);
        toMember.getFollowers().add(following);
    }
}
