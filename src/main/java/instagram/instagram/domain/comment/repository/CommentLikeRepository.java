package instagram.instagram.domain.comment.repository;

import instagram.instagram.domain.comment.entity.Comment;
import instagram.instagram.domain.comment.entity.CommentLike;
import instagram.instagram.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {


    Optional<CommentLike> findByCommentAndMember(Comment comment, Member member);
}
