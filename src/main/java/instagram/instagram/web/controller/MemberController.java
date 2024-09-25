package instagram.instagram.web.controller;


import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberQueryRepository;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.service.MemberService;
import instagram.instagram.web.dto.member.MemberJoinRequest;
import instagram.instagram.web.dto.member.MemberProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MemberQueryRepository memberQueryRepository;

    @GetMapping("/member/{id}")
    public MemberProfileDto findMember(@PathVariable("id") Long id) {
        Member member = memberQueryRepository.findMember(id).get(0);
        return new MemberProfileDto(member);
    }

    @PostMapping("/member/join")
    public Long joinMember(@RequestBody MemberJoinRequest request) {
        Long joinMemberId = memberService.join(request);
        return joinMemberId;
    }

}
