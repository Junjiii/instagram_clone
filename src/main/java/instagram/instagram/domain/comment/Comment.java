package instagram.instagram.domain.comment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import instagram.instagram.domain.baseEntity.BaseTimeEntity;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String comment;

    @JsonIgnore
    @OneToMany(mappedBy = "comment",cascade = CascadeType.ALL)
    private List<CommentLike> commentLikes = new ArrayList<>();

    public Comment(Member member, Post post, String comment) {
        this.member = member;
        this.post = post;
        this.comment = comment;
    }

    //== 연관 관계 메서드 ==//
    public void addCommentLikes(Member member) {
        this.commentLikes.add(new CommentLike(this,member));
    }
}
