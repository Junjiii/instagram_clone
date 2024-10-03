package instagram.instagram.domain.post.repository;

import instagram.instagram.domain.post.entity.Post;
import instagram.instagram.web.dto.post.PostCommentDto;
import instagram.instagram.web.dto.post.PostHashtagDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final EntityManager em;

    public Post findPost(Long id) {
        return em.createQuery("select p from Post p " +
                "join fetch p.member m " +
                "join fetch p.postImages " +
                "where p.id = :id", Post.class)
                .setParameter("id",id)
                .getSingleResult();
    }


    public List<PostHashtagDto> findPostHashtags(Long postId) {
        return em.createQuery("select new instagram.instagram.web.dto.post.PostHashtagDto(h.hashtag) " +
                "from PostHashtag ph " +
                "join hashtag h " +
                "where ph.post.id = :id",PostHashtagDto.class)
                .setParameter("id", postId)
                .getResultList();
    }

    public List<PostCommentDto> findPostCommentDtos(Long postId) {
        return em.createQuery("select new instagram.instagram.web.dto.post" +
                        ".PostCommentDto(c.id, cm.profileImage, cm.nickname, c.comment) " +
                "from Comment c " +
                "join c.member cm " +
                "where c.post.id = :id",PostCommentDto.class)
                .setParameter("id",postId)
                .getResultList();
    }
}
