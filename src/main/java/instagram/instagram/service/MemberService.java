package instagram.instagram.service;

import instagram.instagram.domain.follow.Follow;
import instagram.instagram.domain.follow.FollowRepository;
import instagram.instagram.domain.member.Gender;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberQueryRepository;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.web.dto.member.MemberJoinRequest;
import instagram.instagram.web.dto.member.MemberProfileDto;
import instagram.instagram.web.dto.member.MemberProfilePostDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final FollowRepository followRepository;
    private final EntityManager em;

    @Transactional
    public Long join(MemberJoinRequest memberJoinRequest) {

        checkDuplicateMember(memberJoinRequest);

        Member member = new Member(
                memberJoinRequest.getEmail(),
                memberJoinRequest.getPassword(),
                memberJoinRequest.getName(),
                memberJoinRequest.getPhoneNumber(),
                Gender.of(memberJoinRequest.getGender()),
                LocalDate.of(memberJoinRequest.getYear(), memberJoinRequest.getMonth(), memberJoinRequest.getDay()));

        memberRepository.save(member);

        return member.getId();
    }


    @Transactional
    public Long follow(Long fromMemberId, Long toMemberId) {

        if(fromMemberId.equals(toMemberId)) {
            throw new IllegalArgumentException("본인을 직접 팔로우 할 수 없습니다.");
        }

        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다.")); // 사용 중인 유저
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));; // 사용 중인 유저가 팔로우 하는 유저


        if(followRepository.findByFromMemberAndToMember(fromMember,toMember).isEmpty()) {
            Follow follow = fromMember.addFollowing(toMember);
            followRepository.save(follow);
            return follow.getId();

        } else {
            throw new IllegalArgumentException("이미 팔로우하는 유저입니다.");
        }
    }


    @Transactional
    public void unFollow(Long fromMemberId, Long toMemberId) {
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다.")); // 사용 중인 유저
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));; // 사용 중인 유저가 팔로우 하는 유저

        fromMember.removeFollowing(toMember);
        toMember.removeFollower(fromMember);

        followRepository.deleteByFromMemberAndToMember(fromMember,toMember);
    }

    /**
     * email,name 중복 체크
     */
    public void checkDuplicateMember(MemberJoinRequest memberJoinRequest) {

        if(!memberRepository.findByEmail(memberJoinRequest.getEmail()).isEmpty()) {
            throw new IllegalStateException("이미 존재하는 Email 입니다.");
        }

        if(!memberRepository.findByName(memberJoinRequest.getName()).isEmpty()) {
            throw new IllegalStateException("이미 존재하는 유저 입니다.");
        }

    }



    public MemberProfileDto findMemberProfileDto(Long id) {
        MemberProfileDto memberProfile = memberQueryRepository.findMemberProfileDto_withFollowCount(id);
        List<MemberProfilePostDto> posts = memberQueryRepository.findPost(id);
        memberProfile.setPosts(posts);
        return memberProfile;
    }
}
