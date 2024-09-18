package instagram.instagram.domain;

import instagram.instagram.domain.member.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostTest {

    @Autowired
    EntityManager em;

    @Test
    public void 포스트_생성() throws Exception
    {
        Member user = new Member("user");
    }
}