package instagram.instagram.domain.member;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    public List<Member> findMember(Long id) {
//        return em.createQuery("select m from Member m left join m.posts p left join p.postImages pi where m.id = :id", Member.class).setParameter("id",id).getResultList();
        return em.createQuery("select m from Member m " +
                "join fetch m.posts p " +
//                "join fetch p.postImages pi " +
                "where m.id = :id", Member.class).setParameter("id",id).getResultList();
    }
}
