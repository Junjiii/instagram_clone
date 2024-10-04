package instagram.instagram.domain.post.repository;

import instagram.instagram.domain.post.entity.Post;
import instagram.instagram.web.dto.post.PostCommentDto;
import instagram.instagram.web.dto.post.PostFindDb;
import instagram.instagram.web.dto.post.PostHashtagDto;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostFindDb> findPost(Long id);
    Long countPostLikes(Long postId);
    List<PostHashtagDto> findPostHashtags(Long postId);
    List<PostCommentDto> findPostCommentDtos(Long postId);
}
