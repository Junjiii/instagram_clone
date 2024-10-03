package instagram.instagram.domain.member.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import instagram.instagram.domain.follow.entity.QFollow;
import instagram.instagram.domain.member.entity.QMember;
import instagram.instagram.domain.post.entity.QPost;
import instagram.instagram.domain.post.entity.QPostImage;
import instagram.instagram.web.dto.member.MemberProfileDto;
import instagram.instagram.web.dto.member.MemberProfilePostDto;
import instagram.instagram.web.dto.member.QMemberProfileDto;
import instagram.instagram.web.dto.member.QMemberProfilePostDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static instagram.instagram.domain.follow.entity.QFollow.follow;
import static instagram.instagram.domain.member.entity.QMember.*;
import static instagram.instagram.domain.post.entity.QPost.post;
import static instagram.instagram.domain.post.entity.QPostImage.postImage;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public MemberProfileDto findMemberProfileDto_withFollowCount(Long id) {
        return queryFactory.select(new QMemberProfileDto(
                member.id,
                member.nickname,
                member.profileImage,
                member.bio,
                Expressions.as(JPAExpressions
                        .select(follow.count())
                        .from(follow)
                        .where(follow.fromMember.id.eq(member.id)),"followings"),
                Expressions.as(JPAExpressions
                        .select(follow.count())
                        .from(follow)
                        .where(follow.toMember.id.eq(member.id)),"followers"))
                ).from(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<MemberProfilePostDto> findPost(Long memberId, int offset, int limit) {
        return queryFactory.select(new QMemberProfilePostDto(
                post.id,
                postImage.image_URL)
                ).from(post)
                .leftJoin(post.postImages, postImage)
                .where(post.member.id.eq(memberId),postImage.sequence.eq(1))
                .orderBy(post.id.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
