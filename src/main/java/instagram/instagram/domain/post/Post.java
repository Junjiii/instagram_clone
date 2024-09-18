package instagram.instagram.domain.post;

import instagram.instagram.domain.baseEntity.BaseTimeEntity;
import instagram.instagram.domain.comment.Comment;
import instagram.instagram.domain.member.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


    public Post(String content, Member member) {
        this.content = content;
        this.member = member;
    }


    // == 연관 관계 메서드 ==//

    public void addPostImages(List<String> imageUrls) {
        int sequence = 1;
        for (String imageUrl : imageUrls) {
            postImages.add(new PostImage(this, imageUrl, sequence));
            sequence++;
        }
    }

    public void addPostHashTags(PostHashtag postHashtag) {
        postHashtags.add(postHashtag);
    }
}
