package instagram.instagram.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member")
    private Member fromMember;   // follow 거는 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member")
    private Member toMember;     // follow 당하는 사람

    public Follow(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }
}
