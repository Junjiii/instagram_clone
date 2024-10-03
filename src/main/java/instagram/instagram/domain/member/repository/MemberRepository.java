package instagram.instagram.domain.member.repository;

import instagram.instagram.domain.member.entity.Member;
import instagram.instagram.web.dto.member.MemberProfileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom {
    List<Member> findByName(String name);
    List<Member> findByEmail(String email);
}
