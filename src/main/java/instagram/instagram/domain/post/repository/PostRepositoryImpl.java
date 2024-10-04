package instagram.instagram.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import instagram.instagram.domain.comment.entity.QComment;
import instagram.instagram.domain.hashtag.entity.QHashtag;
import instagram.instagram.domain.member.entity.QMember;
import instagram.instagram.domain.post.entity.*;
import instagram.instagram.web.dto.post.*;
import jakarta.persistence.EntityManager;

import java.util.List;

import static instagram.instagram.domain.comment.entity.QComment.comment1;
import static instagram.instagram.domain.hashtag.entity.QHashtag.*;
import static instagram.instagram.domain.member.entity.QMember.member;
import static instagram.instagram.domain.post.entity.QPost.post;
import static instagram.instagram.domain.post.entity.QPostHashtag.postHashtag;
import static instagram.instagram.domain.post.entity.QPostImage.postImage;
import static instagram.instagram.domain.post.entity.QPostLike.postLike;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<PostFindDb> findPost(Long id) {
        return queryFactory.select(new QPostFindDb(
                post.id,
                post.content,
                post.member.nickname,
                post.member.profileImage,
                postImage.image_URL
                ))
                .from(post)
                .join(post.member,member)
                .join(post.postImages,postImage)
                .where(post.id.eq(id))
                .fetch();
    }


    @Override
    public Long countPostLikes(Long postId) {
        return queryFactory.select(postLike.count())
                .from(postLike)
                .where(postLike.post.id.eq(postId))
                .fetchOne();
    }

    @Override
    public List<PostHashtagDto> findPostHashtags(Long postId) {
        return queryFactory.select(new QPostHashtagDto(
                postHashtag.hashtag.hashtag))
                .from(postHashtag)
                .where(postHashtag.post.id.eq(postId))
                .fetch();
    }

    @Override
    public List<PostCommentDto> findPostCommentDtos(Long postId) {
        return queryFactory.select(
                new QPostCommentDto(
                        comment1.id,
                        comment1.member.profileImage,
                        comment1.member.nickname,
                        comment1.comment))
                .from(comment1)
                .where(comment1.post.id.eq(postId))
                .fetch();
    }
}
