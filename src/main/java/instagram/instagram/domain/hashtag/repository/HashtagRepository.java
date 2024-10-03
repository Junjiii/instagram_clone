package instagram.instagram.domain.hashtag.repository;

import instagram.instagram.domain.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag,Long> {

    Hashtag findByHashtag(String hashtag);
}
