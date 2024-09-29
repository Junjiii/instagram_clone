package instagram.instagram.web.controller;


import instagram.instagram.domain.member.MemberQueryRepository;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.service.MemberService;
import instagram.instagram.web.dto.member.MemberJoinRequest;
import instagram.instagram.web.dto.member.MemberProfileDto;
import instagram.instagram.web.dto.member.MemberProfilePostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MemberQueryRepository memberQueryRepository;

    @GetMapping("/member/{id}")
    public MemberProfileDto findMemberProfile(@PathVariable("id") Long id) {
        return memberService.findMemberProfileDto(id);
    }


    @PostMapping("/member/join")
    public Long joinMember(@RequestBody MemberJoinRequest request) {
        Long joinMemberId = memberService.join(request);
        return joinMemberId;
    }

}
