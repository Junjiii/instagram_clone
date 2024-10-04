package instagram.instagram.domain.member.repository;

import instagram.instagram.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom {
    List<Member> findByName(String name);
    List<Member> findByEmail(String email);
}
