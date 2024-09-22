package instagram.instagram.service;

import instagram.instagram.domain.comment.Comment;
import instagram.instagram.domain.comment.CommentLikeRepository;
import instagram.instagram.domain.comment.CommentRepository;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.post.Post;
import instagram.instagram.web.dto.member.MemberJoinReqDto;
import instagram.instagram.web.dto.post.PostCreateReqDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Rollback
class CommentServiceTest {

    @Autowired CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentLikeRepository commentLikeRepository;
    @Autowired MemberService memberService;
    @Autowired PostService postService;
    @Autowired
    EntityManager em;


    @Test
    public void 코멘트_생성() throws Exception
    {
        MemberJoinReqDto memberJoinReqDto = new MemberJoinReqDto(
                "email@gmail.com",
                "password",
                "user1",
                "010-1111-1111",
                "male",
                2024,
                9,
                17);
        Member member = memberService.join(memberJoinReqDto);


        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag1");

        PostCreateReqDto postCreateReqDto = new PostCreateReqDto("post1", imageUrls, hashtags);

        Post post = postService.createPost(member.getId(), postCreateReqDto);

        Comment comment = commentService.createComment(member.getId(), post.getId(), "comment1");

        commentRepository.save(comment);

        em.flush();
        em.clear();

        Comment findComment = commentRepository.findById(comment.getId()).get();

        assertThat(findComment.getId()).isEqualTo(1L);
        assertThat(findComment.getMember().getId()).isEqualTo(member.getId());
        assertThat(findComment.getPost().getId()).isEqualTo(post.getId());
        assertThat(findComment.getComment()).isEqualTo("comment1");

        assertThat(post.getComments().get(0).getId()).isEqualTo(findComment.getId());
    }


    @Test
    public void 코멘트_코멘트_라이크_생성() throws Exception
    {
        MemberJoinReqDto memberJoinReqDto1 = new MemberJoinReqDto(
                "email@gmail.com",
                "password",
                "user1",
                "010-1111-1111",
                "male",
                2024,
                9,
                17);

        MemberJoinReqDto memberJoinReqDto2 = new MemberJoinReqDto(
                "email2@gmail.com",
                "password",
                "user2",
                "010-1111-1111",
                "male",
                2024,
                9,
                17);
        Member member1 = memberService.join(memberJoinReqDto1);
        Member member2 = memberService.join(memberJoinReqDto2);


        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag1");

        PostCreateReqDto postCreateReqDto = new PostCreateReqDto("post1", imageUrls, hashtags);

        Post post = postService.createPost(member1.getId(), postCreateReqDto);

        Comment comment = commentService.createComment(member2.getId(), post.getId(), "commentByMember2");

        commentRepository.save(comment);

        em.flush();
        em.clear();

        Comment commentByMember2 = commentRepository.findById(comment.getId()).get();

        commentService.createCommentLike(member1.getId(), comment.getId());
        assertThat(commentByMember2.getCommentLikes().size()).isEqualTo(1);
        assertThat(commentByMember2.getCommentLikes().get(0).getMember().getId()).isEqualTo(member1.getId());
        assertThat(commentByMember2.getCommentLikes().get(0).getComment().getId()).isEqualTo(commentByMember2.getId());
    }


    @Test
    public void 코멘트_라이크_예외() throws Exception
    {
        MemberJoinReqDto memberJoinReqDto1 = new MemberJoinReqDto(
                "email1@gmail.com",
                "password",
                "user1",
                "010-1111-1111",
                "male",
                2024,
                9,
                17);


        MemberJoinReqDto memberJoinReqDto2 = new MemberJoinReqDto(
                "email2@gmail.com",
                "password",
                "user2",
                "010-1111-1111",
                "male",
                2024,
                9,
                17);

        Member member1 = memberService.join(memberJoinReqDto1);
        Member member2 = memberService.join(memberJoinReqDto2);




        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag1");

        PostCreateReqDto postCreateReqDto = new PostCreateReqDto("post1", imageUrls, hashtags);

        Post post = postService.createPost(member1.getId(), postCreateReqDto);

        Comment comment = commentService.createComment(member2.getId(), post.getId(), "commentByMember2");

        em.flush();
        em.clear();

        commentService.createCommentLike(member2.getId(),comment.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            commentService.createCommentLike(member2.getId(),comment.getId());
        });

    }


}