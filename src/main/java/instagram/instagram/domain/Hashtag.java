package instagram.instagram.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Hashtag extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "hashtag_id")
    private Long id;

    private String hashtag;
}
