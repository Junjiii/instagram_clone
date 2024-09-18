package instagram.instagram.service;

import instagram.instagram.domain.follow.FollowRepository;
import instagram.instagram.domain.member.Gender;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.web.dto.member.MemberJoinReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public Member join(MemberJoinReqDto memberJoinReqDto) {

        checkDuplicateMember(memberJoinReqDto);

        Member member = new Member(
                memberJoinReqDto.getEmail(),
                memberJoinReqDto.getPassword(),
                memberJoinReqDto.getName(),
                memberJoinReqDto.getPhoneNumber(),
                Gender.of(memberJoinReqDto.getGender()),
                LocalDate.of(memberJoinReqDto.getYear(), memberJoinReqDto.getMonth(), memberJoinReqDto.getDay()));

        return memberRepository.save(member);
    }

    public void unFollow(Long fromMemberId, Long toMemberId) {
        Member fromMember = memberRepository.findById(fromMemberId).get(); // 사용 중인 유저
        Member toMember = memberRepository.findById(toMemberId).get(); // 사용 중인 유저가 팔로우 하는 유저

        fromMember.removeFollowing(toMember);
        toMember.removeFollower(fromMember);

        followRepository.deleteByFromMemberAndToMember(fromMember,toMember);
    }

    /**
     * email,name 중복 체크
     */
    public void checkDuplicateMember(MemberJoinReqDto memberJoinReqDto) {

        if(!memberRepository.findByEmail(memberJoinReqDto.getEmail()).isEmpty()) {
            throw new IllegalStateException("이미 존재하는 Email 입니다.");
        }

        if(!memberRepository.findByName(memberJoinReqDto.getName()).isEmpty()) {
            throw new IllegalStateException("이미 존재하는 유저 입니다.");
        }

    }
}
