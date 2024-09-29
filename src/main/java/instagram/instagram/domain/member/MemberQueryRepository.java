package instagram.instagram.domain.member;


import instagram.instagram.web.dto.member.MemberProfileDto;
import instagram.instagram.web.dto.member.MemberProfilePostDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    public MemberProfileDto findMemberProfileDto(Long id) {
        MemberProfileDto memberProfile = findMemberProfileDto_withFollowCount(id);
        List<MemberProfilePostDto> posts = findPost(id);
        memberProfile.setPosts(posts);
        return memberProfile;
    }

    public MemberProfileDto findMemberProfileDto_withFollowCount(Long id) {
        return em.createQuery("select new instagram.instagram.web.dto.member.MemberProfileDto(m.id, m.nickname, m.profileImage, m.bio, " +
                        "(SELECT COUNT(fwg) FROM Follow fwg WHERE fwg.fromMember.id = m.id) as followings, " +  // m이 팔로우한 수
                        "(SELECT COUNT(fwr) FROM Follow fwr WHERE fwr.toMember.id = m.id) as followers ) " +  // m을 팔로우한 수
                        "from Member m " +
                        "where m.id = :id", MemberProfileDto.class)
                .setParameter("id",id)
                .getSingleResult();
    }


    public List<MemberProfilePostDto> findPost(Long memberId) {
        return em.createQuery("select new instagram.instagram.web.dto.member.MemberProfilePostDto(p.id, pi.image_URL) " +
                "from Post p " +
                "join fetch PostImage pi " +
                "on p.id = pi.post.id " +
                "where p.member.id = :id " +
                "and pi.sequence = 1 " +
                "order by p.id desc",MemberProfilePostDto.class)
                .setParameter("id", memberId)
                .getResultList();
    }

}
