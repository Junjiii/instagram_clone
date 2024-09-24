package instagram.instagram;


import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.post.Post;
import instagram.instagram.service.MemberService;
import instagram.instagram.service.PostService;
import instagram.instagram.web.dto.member.MemberJoinReqDto;
import instagram.instagram.web.dto.post.PostCreateReqDto;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void inti() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final MemberService memberService;
        private final PostService postService;

        public void dbInit() {
            for (int i = 1; i < 6; i++) {
                Long id = createMember("email" + i + "@gmail.com", "user" + i);
                int random1 = (int) (Math.random() * 5) + 1;
                for (int j = 1; j < random1; j++) {
                    int random2 = (int) (Math.random() * 7) + 1;
                    int random3 = (int) (Math.random() * 7) + 1;
                    createPostDto("content" + j, random2, random3, id);
                }
            }

        }


        public Long createMember(String email, String name) {
            MemberJoinReqDto memberJoinReqDto = new MemberJoinReqDto(
                    email,
                    "password",
                    name,
                    "010-1111-1111",
                    "male",
                    2024,
                    11,
                    11
            );

            return memberService.join(memberJoinReqDto);
        }


        public void createPostDto(String content , int urlSequence, int hashtagSequence, Long memberId) {
            ArrayList<String> imageUrls = new ArrayList<>();
            ArrayList<String> hashtags = new ArrayList<>();

            for (int i = 1; i < urlSequence+1; i++) {
                imageUrls.add("url" + i);
            }

            for (int i = 1; i < hashtagSequence+1; i++) {
                hashtags.add("#Hashtag" + i);
            }

            PostCreateReqDto postCreateReqDto = new PostCreateReqDto(content, imageUrls, hashtags);
            postService.createPost(memberId, postCreateReqDto);
        }
    }
}
