package instagram.instagram.domain.post;


import instagram.instagram.domain.baseEntity.BaseTimeEntity;
import instagram.instagram.domain.hashtag.Hashtag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PostHashtag extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_hashtag_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @Builder
    public PostHashtag(Post post, Hashtag hashtag) {
        this.post = post;
        this.hashtag = hashtag;
    }

}
