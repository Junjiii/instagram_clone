package instagram.instagram.domain.follow.repository;


import instagram.instagram.domain.follow.entity.Follow;
import instagram.instagram.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Modifying
    void deleteByFromMemberAndToMember(Member fromMember, Member toMember);

    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
