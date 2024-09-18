package instagram.instagram.domain.post;

import instagram.instagram.domain.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PostImage extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String image_URL;

    private int sequence;

    public PostImage(Post post, String image_URL, int sequence) {
        this.post = post;
        this.image_URL = image_URL;
        this.sequence = sequence;
    }
}
