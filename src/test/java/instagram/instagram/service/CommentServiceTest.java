package instagram.instagram.service;

import instagram.instagram.domain.comment.Comment;
import instagram.instagram.domain.comment.CommentLikeRepository;
import instagram.instagram.domain.comment.CommentRepository;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.domain.post.Post;
import instagram.instagram.domain.post.PostRepository;
import instagram.instagram.web.dto.member.MemberJoinRequest;
import instagram.instagram.web.dto.post.PostCreateRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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
    @Autowired
    MemberRepository memberRepository;
    @Autowired PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    EntityManager em;


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void 코멘트_생성() throws Exception
    {
        Member member = createMemberDto("email@gmail.com", "user");
        Post post = createPostDto("content", 1, 1, member.getId());

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
        Member member1 = createMemberDto("email1@gmail.com", "user1");
        Member member2 = createMemberDto("email2@gmail.com", "user2");
        Post post = createPostDto("content", 1, 1, member1.getId());

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
        Member member1 = createMemberDto("email1@gmail.com", "user1");
        Member member2 = createMemberDto("email2@gmail.com", "user2");
        Post post = createPostDto("content", 1, 1, member1.getId());

        Comment comment = commentService.createComment(member2.getId(), post.getId(), "commentByMember2");

        em.flush();
        em.clear();

        commentService.createCommentLike(member2.getId(),comment.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            commentService.createCommentLike(member2.getId(),comment.getId());
        });

    }

    public Member createMemberDto(String email, String name) {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest(
                email,
                "password",
                name,
                "010-1111-1111",
                "male",
                2024,
                11,
                11
        );

        Long savedMemberId = memberService.join(memberJoinRequest);
        return memberRepository.findById(savedMemberId).get();

    }


    public Post createPostDto(String content ,int urlSequence, int hashtagSequence, Long memberId) {
        ArrayList<String> imageUrls = new ArrayList<>();
        ArrayList<String> hashtags = new ArrayList<>();

        for (int i = 1; i < urlSequence+1; i++) {
            imageUrls.add("url" + i);
        }

        for (int i = 1; i < hashtagSequence+1; i++) {
            hashtags.add("#Hashtag" + i);
        }

        PostCreateRequest postCreateRequest = new PostCreateRequest(content, imageUrls, hashtags);
        Long savedPostId = postService.createPost(memberId, postCreateRequest);
        return postRepository.findById(savedPostId).get();
    }


}