package instagram.instagram.domain;

import instagram.instagram.domain.member.entity.Gender;
import instagram.instagram.domain.member.entity.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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

}