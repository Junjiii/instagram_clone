package instagram.instagram.domain.member;

import instagram.instagram.web.dto.member.MemberProfileDto2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {

    List<Member> findByName(String name);
    List<Member> findByEmail(String email);

//    @Query(value = "SELECT M.*, P.*, PI.* FROM MEMBER AS M " +
//            "LEFT JOIN " +
//            "POST AS P " +
//            "ON M.ID = P.MEMBER_ID " +
//            "LEFT JOIN " +
//            "POST_IMAGE AS PI " +
//            "ON P.ID = PI.POST_ID " +
//            "LEFT JOIN " +
//            "(SELECT COUNT(*) AS FROM_MEMBER_COUNT FROM FOLLOW GROUP BY FROM_MEMBER AS FWR " +
//            "ON (M.ID = FWR.FROM_MEMBER) " +
//            "LEFT JOIN " +
//            "(SELECT COUNT(*) AS TO_MEMBER_COUNT FROM FOLLOW GROUP BY TO_MEMBER AS FWG " +
//            "ON (M.ID = FWG.TO_MEMBER) " +
//            "WHERE M.ID = :id", nativeQuery = true)


    @Query(value = "SELECT M.*, P.POST_ID, P.CONTENT, PI.IMAGE_URL, FWR.FROM_MEMBER_COUNT, FWG.TO_MEMBER_COUNT " +
            "FROM MEMBER AS M " +
            "LEFT JOIN POST AS P ON M.MEMBER_ID = P.MEMBER_ID " +
            "LEFT JOIN POST_IMAGE AS PI ON P.POST_ID = PI.POST_ID " +
            "LEFT JOIN (" +
            "    SELECT FROM_MEMBER, COUNT(*) AS FROM_MEMBER_COUNT " +
            "    FROM FOLLOW " +
            "    GROUP BY FROM_MEMBER" +
            ") AS FWR ON M.MEMBER_ID = FWR.FROM_MEMBER " +
            "LEFT JOIN (" +
            "    SELECT TO_MEMBER, COUNT(*) AS TO_MEMBER_COUNT " +
            "    FROM FOLLOW " +
            "    GROUP BY TO_MEMBER" +
            ") AS FWG ON M.MEMBER_ID = FWG.TO_MEMBER " +
            "WHERE M.MEMBER_ID = :id", nativeQuery = true)
    List<Object[]> findMemberProfileById(@Param("id") Long id);
}
