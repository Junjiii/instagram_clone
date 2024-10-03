package instagram.instagram.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import instagram.instagram.domain.baseEntity.BaseTimeEntity;
import instagram.instagram.domain.comment.entity.Comment;
import instagram.instagram.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String content;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostLike> postLikes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


    public Post(String content, Member member) {
        this.content = content;
        this.member = member;
    }


    // == 연관 관계 메서드 ==//

    public void addPostImages(String imageUrl, int sequence) {
        postImages.add(new PostImage(this, imageUrl, sequence));
    }

    public void addPostHashTags(PostHashtag postHashtag) {
        postHashtags.add(postHashtag);
    }

    public void addPostLikes(Member member) {
        PostLike postLike = new PostLike(this, member);
        this.postLikes.add(postLike);
    }

    public void addComments(Comment comment) {
        this.comments.add(comment);
    }
}
