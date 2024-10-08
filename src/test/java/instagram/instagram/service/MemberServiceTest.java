package instagram.instagram.service;

import instagram.instagram.domain.follow.repository.FollowRepository;
import instagram.instagram.domain.member.entity.Gender;
import instagram.instagram.domain.member.entity.Member;
import instagram.instagram.domain.member.repository.MemberRepository;
import instagram.instagram.web.dto.member.MemberJoinRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    EntityManager em;


    @Test
    public void 회원가입() throws Exception
    {
        // given
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest(
                "email@gmamil.com",
                "password",
                "user",
                "010-1111-1111",
                "male",
                2024,
                11,
                11
        );
        Long savedMemberId = memberService.join(memberJoinRequest);


        // when
        em.flush();
        em.clear();
        Member findMember = memberRepository.findById(savedMemberId).get();

        // then
        assertThat(findMember.getId()).isEqualTo(savedMemberId);
        assertThat(findMember.getEmail()).isEqualTo(memberJoinRequest.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(memberJoinRequest.getPassword());
        assertThat(findMember.getName()).isEqualTo(memberJoinRequest.getName());
        assertThat(findMember.getPhoneNumber()).isEqualTo(memberJoinRequest.getPhoneNumber());
        assertThat(findMember.getGender()).isEqualTo(Gender.MALE);
        assertThat(findMember.getBirth()).isEqualTo(LocalDate.of(2024,11,11));
        // 최초 생성시 nickname의 default 값은 기본 name 값
        assertThat(findMember.getName()).isEqualTo(findMember.getNickname());
    }


    @Test
    public void 중복_회원() throws Exception
    {
        // given
        Member member1 = createMemberDto("email1@gmail.com", "user1");

        em.flush();
        em.clear();

        assertThrows(IllegalStateException.class, () -> {
            createMemberDto("email1@gmail.com", "user1");
        });

    }


    @Test
    public void 추후_데이터추가() throws Exception
    {

        Member joinMember = createMemberDto("email@gmail.com", "user");

        // 추후 데이터 변경 ( 닉네임 설정, 프로필 이미지 설정, Bio 설정 )
        joinMember.setNickname("newNickname");
        joinMember.setProfileImage("asgdjkhaskgsskshalkfhjsa");
        joinMember.setBio("Hello, World");

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, joinMember.getId());


        // 데이터 변경 테스트 ( 닉네임, 프로필 이미지, Bio)
        assertThat(findMember.getNickname()).isEqualTo("newNickname");
        assertThat(findMember.getProfileImage()).isEqualTo("asgdjkhaskgsskshalkfhjsa");
        assertThat(findMember.getBio()).isEqualTo("Hello, World");
    }



    @Test
    public void 멤버_팔로우() throws Exception
    {
        Member member1 = createMemberDto("email1@gmail.com", "user1");
        Member member2 = createMemberDto("email2@gmail.com", "user2");
        Member member3 = createMemberDto("email3@gmail.com", "user3");
        Member member4 = createMemberDto("email4@gmail.com", "user4");


        // member1 -> member2,3,4 팔로우
        memberService.follow(member1.getId(), member2.getId());
        memberService.follow(member1.getId(), member3.getId());
        memberService.follow(member1.getId(), member4.getId());


        // 영속성 컨텍스트 날리기
        em.flush();
        em.clear();

        Member user1 = memberRepository.findById(member1.getId()).get();
        Member user2 = memberRepository.findById(member2.getId()).get();
        Member user3 = memberRepository.findById(member3.getId()).get();
        Member user4 = memberRepository.findById(member4.getId()).get();

        assertThat(user1.getFollowings().size()).isEqualTo(3);
        assertThat(user1.getFollowings().get(0)).isEqualTo(user2.getFollowers().get(0));
        assertThat(user1.getFollowings().get(1)).isEqualTo(user3.getFollowers().get(0));
        assertThat(user1.getFollowings().get(2)).isEqualTo(user4.getFollowers().get(0));
    }

    @Test
    public void 멤버_팔로우_본인팔로우예외() throws Exception
    {
        Long fromMember = 1L;
        Long toMember = 1L;

        assertThrows(IllegalArgumentException.class, () -> memberService.follow(fromMember,toMember));
    }

    @Test
    public void 멤버_팔로우_중복팔로예외() throws Exception
    {
        Member member1 = createMemberDto("email1@gmail.com", "user1");
        Member member2 = createMemberDto("email2@gmail.com", "user2");

        // member1 -> member2,3,4 팔로우
        memberService.follow(member1.getId(), member2.getId());

        // 영속성 컨텍스트 날리기
        em.flush();
        em.clear();

        Member user1 = memberRepository.findById(member1.getId()).get();
        Member user2 = memberRepository.findById(member2.getId()).get();

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.follow(member1.getId(), member2.getId());
        });

    }

    @Test
    public void 멤버_언팔로우() throws Exception
    {
        Member member1 = createMemberDto("email1@gmail.com", "user1");
        Member member2 = createMemberDto("email2@gmail.com", "user2");
        Member member3 = createMemberDto("email3@gmail.com", "user3");
        Member member4 = createMemberDto("email4@gmail.com", "user4");


        // member1 -> member2,3,4 팔로우
        member1.addFollowing(member2);
        member1.addFollowing(member3);
        member1.addFollowing(member4);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // 영속성 컨텍스트 날리기
        em.flush();
        em.clear();

        // member1 -> member2,3,4, 언팔로우
        memberService.unFollow(member1.getId(), member2.getId());
        memberService.unFollow(member1.getId(), member3.getId());
        memberService.unFollow(member1.getId(), member4.getId());


        // 영속성 컨텍스트 날리기
        em.flush();
        em.clear();

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Member findMember3 = memberRepository.findById(member3.getId()).get();
        Member findMember4 = memberRepository.findById(member4.getId()).get();

        // 팔로잉 삭제 테스트
        assertThat(findMember1.getFollowings().size()).isEqualTo(0);
        // 팔로워 삭제 테스트
        assertThat(findMember2.getFollowers().size()).isEqualTo(0);
        assertThat(findMember3.getFollowers().size()).isEqualTo(0);
        assertThat(findMember4.getFollowers().size()).isEqualTo(0);
    }


    @Test
    public void 멤버_언팔로우_예외() throws Exception
    {
        Member member1 = createMemberDto("email1@gmail.com", "user1");
        Member member2 = createMemberDto("email2@gmail.com", "user2");
        Member member3 = createMemberDto("email3@gmail.com", "user3");
        Member member4 = createMemberDto("email4@gmail.com", "user4");


        // member1 -> member2 팔로우
        member1.addFollowing(member2);

        // 영속성 컨텍스트 날리기
        em.flush();
        em.clear();


        // member1 -> member3 언팔로우 ( 예외 터짐 )
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.unFollow(member1.getId(), member3.getId());
        });
    }



    public Member createMemberDto(String email, String name) {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest(
                email,
                "password",
                name,
                "010-1111-1111",
                "male",
                2024,
                11,
                11
        );

        Long savedMemberId = memberService.join(memberJoinRequest);
        return memberRepository.findById(savedMemberId).get();
    }
}

