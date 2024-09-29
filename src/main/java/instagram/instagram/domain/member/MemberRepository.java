package instagram.instagram.domain.member;

import instagram.instagram.web.dto.member.MemberProfileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {

    List<Member> findByName(String name);
    List<Member> findByEmail(String email);

    @Query(value = "SELECT MEMBER.*," +
            "POST_COUNT, P.POST_ID, PI.IMAGE_URL, FWR.FOLLOWER_COUNT, FWG.FOLLOWING_COUNT FROM MEMBER " +
            "INNER JOIN " +
            "(SELECT POST.MEMBER_ID, POST.POST_ID, POST.CONTENT, COUNT(*) AS POST_COUNT FROM POST GROUP BY POST.POST_ID) AS P " +
            "ON MEMBER.MEMBER_ID = P.MEMBER_ID " +
            "INNER JOIN " +
            "POST_IMAGE AS PI " +
            "ON P.POST_ID = PI.POST_ID " +
            "INNER JOIN " +
            "(SELECT FROM_MEMBER, COUNT(*)  AS FOLLOWER_COUNT FROM FOLLOW GROUP BY FROM_MEMBER) AS FWR " +
            "ON MEMBER.MEMBER_ID = FWR.FROM_MEMBER " +
            "INNER JOIN " +
            "(SELECT  TO_MEMBER, COUNT(*)  AS FOLLOWING_COUNT FROM FOLLOW GROUP BY TO_MEMBER) AS FWG " +
            "ON MEMBER.MEMBER_ID = FWG.TO_MEMBER " +
            "WHERE MEMBER.MEMBER_ID = :id;",nativeQuery = true)
    List<MemberProfileDto> findMemberProfileById(@Param("id") Long id);
}
