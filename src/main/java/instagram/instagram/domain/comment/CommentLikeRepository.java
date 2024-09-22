package instagram.instagram.domain.comment;

import com.querydsl.core.Tuple;
import instagram.instagram.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {


    Optional<CommentLike> findByCommentAndMember(Comment comment, Member member);
}
