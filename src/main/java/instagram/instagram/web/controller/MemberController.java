package instagram.instagram.web.controller;


import instagram.instagram.domain.member.Gender;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.domain.post.Post;
import instagram.instagram.service.MemberService;
import instagram.instagram.web.dto.member.MemberJoinReqDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/member/{id}")
    public ResponseMember findMember(@PathVariable("id") Long id) {
        Member member = memberService.findMember(id).get(0);
        return new ResponseMember(member);
    }

    @PostMapping("/member/join")
    public Long joinMember(@RequestBody MemberJoinReqDto request) {
        Long joinMemberId = memberService.join(request);
        return joinMemberId;
    }

    @Getter
    static class ResponseMember {
        private Long id;
        private String email;
        private String password;
        private String name;
        private String phoneNumber;
        private Gender gender;
        private LocalDate birth;
        private String nickname;
        private String profileImage;
        private String bio;
//        private List<Post> posts;
        private int posts;

        public ResponseMember(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.password = member.getPassword();
            this.name = member.getName();
            this.phoneNumber = member.getPhoneNumber();
            this.gender = member.getGender();
            this.birth = member.getBirth();
            this.nickname = member.getNickname();
            this.profileImage = member.getProfileImage();
            this.bio = member.getBio();
            this.posts = member.getPosts().size();
        }
    }
}
