package instagram.instagram.domain;

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
class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    public void 멤버_저장() throws Exception
    {
        // given
        Member member = new Member(
                "test@gamil.com",
                "laksjfhkjsdkafs",
                "test",
                "010-2991-1701",
                Gender.MALE,
                LocalDate.of(1995, 11, 06));
        em.persist(member);
        em.flush();
        em.clear();

        // when

        Member findMember = em.find(Member.class, member.getId());
        // member 와 findMember 가 같은지 여부
        assertThat(member.getId()).isEqualTo(findMember.getId());

        // 최초 생성시 nickname의 default 값은 기본 name 값
        assertThat(findMember.getName()).isEqualTo(findMember.getNickname());

        // 추후 데이터 변경 ( 닉네임 설정, 프로필 이미지 설정, Bio 설정 )
        findMember.setNickname("newTest");
        findMember.setProfileImage("asgdjkhaskgsskshalkfhjsa");
        findMember.setBio("Hello, World");

        em.flush();
        em.clear();
        Member newFindMember = em.find(Member.class, member.getId());


        // 데이터 변경 테스트 ( 닉네임, 프로필 이미지, Bio)
        assertThat(newFindMember.getNickname()).isEqualTo("newTest");
        assertThat(newFindMember.getProfileImage()).isEqualTo("asgdjkhaskgsskshalkfhjsa");
        assertThat(newFindMember.getBio()).isEqualTo("Hello, World");
    }

    @Test
    public void 멤버_팔로우() throws Exception
    {
        // given
        Member member1 = new Member("user1");
        Member member2 = new Member("user2");
        Member member3 = new Member("user3");
        Member member4 = new Member("user4");
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

    // user1 -> user2, user3,user4 팔로우
        member1.addFollowing(member2);
        member1.addFollowing(member3);
        member1.addFollowing(member4);

        // 영속성 컨텍스트 날리기
        em.flush();
        em.clear();

        Member findMember1 = em.find(Member.class, member1.getId());
        Member findMember2 = em.find(Member.class, member2.getId());
        Member findMember3 = em.find(Member.class, member3.getId());
        Member findMember4 = em.find(Member.class, member4.getId());


        assertThat(findMember1.getFollowings().size()).isEqualTo(3);
        assertThat(findMember1.getFollowings().get(0)).isEqualTo(findMember2.getFollowers().get(0));
        assertThat(findMember1.getFollowings().get(1)).isEqualTo(findMember3.getFollowers().get(0));
        assertThat(findMember1.getFollowings().get(2)).isEqualTo(findMember4.getFollowers().get(0));
    }
}