package instagram.instagram.service;

import instagram.instagram.domain.comment.entity.Comment;
import instagram.instagram.domain.comment.repository.CommentLikeRepository;
import instagram.instagram.domain.comment.repository.CommentRepository;
import instagram.instagram.domain.member.entity.Member;
import instagram.instagram.domain.member.repository.MemberRepository;
import instagram.instagram.domain.post.entity.Post;
import instagram.instagram.domain.post.repository.PostRepository;
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
    private final CommentLikeRepository commentLikeRepository;

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

        if (commentLikeRepository.findByCommentAndMember(comment, member).isEmpty()) {
            comment.addCommentLikes(member);
        } else {
            throw new IllegalArgumentException("이미 라이크가 존재합니다.");
        }
    }
}
