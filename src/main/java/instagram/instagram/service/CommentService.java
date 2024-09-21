package instagram.instagram.service;

import instagram.instagram.domain.comment.Comment;
import instagram.instagram.domain.comment.CommentRepository;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.domain.post.Post;
import instagram.instagram.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment createComment(Long memberId, Long postId, String content) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("일치하는 포스트가 없습니다."));

        Comment comment = new Comment(member, post, content);
        post.addComments(comment);

        return comment;
    }

    @Transactional
    public void createCommentLike(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("일치하는 코멘트가 없습니다."));

        comment.addCommentLikes(member);
    }
}
