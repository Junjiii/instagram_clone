package instagram.instagram.domain.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag,Long> {

    Hashtag findByHashtag(String hashtag);
}
