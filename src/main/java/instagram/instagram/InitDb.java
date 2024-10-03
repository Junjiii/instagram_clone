package instagram.instagram;


import instagram.instagram.service.CommentService;
import instagram.instagram.service.MemberService;
import instagram.instagram.service.PostService;
import instagram.instagram.web.dto.member.MemberJoinRequest;
import instagram.instagram.web.dto.post.PostCreateRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final MemberService memberService;
        private final PostService postService;
        private final CommentService commentService;

        public void dbInit() {
            for (int i = 1; i < 6; i++) {
                Long id = createMember("email" + i + "@gmail.com", "user" + i);
//                int random1 = (int) (Math.random() * 5) + 1;
                for (int j = 1; j < 4; j++) {
//                    int random2 = (int) (Math.random() * 7) + 1;
//                    int random3 = (int) (Math.random() * 7) + 1;
                    createPostDto("content" + j, 3, 4, id);
                }
            }
            follow();
            createComment(3,3);
            createPostLike(2,3);


        }

//        public void dbInit() {
//            for (int i = 1; i < 6; i++) {
//                Long id = createMember("email" + i + "@gmail.com", "user" + i);
//                int random1 = (int) (Math.random() * 5) + 1;
//                for (int j = 1; j < random1; j++) {
//                    int random2 = (int) (Math.random() * 7) + 1;
//                    int random3 = (int) (Math.random() * 7) + 1;
//                    createPostDto("content" + j, random2, random3, id);
//                }
//            }
//
//            follow();
//
//        }


        public Long createMember(String email, String name) {
            MemberJoinRequest memberJoinRequest = new MemberJoinRequest(
                    email,
                    "password",
                    name,
                    "010-1111-1111",
                    "male",
                    2024,
                    11,
                    11
            );

            return memberService.join(memberJoinRequest);
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

            PostCreateRequest postCreateRequest = new PostCreateRequest(content, imageUrls, hashtags);
            postService.createPost(memberId, postCreateRequest);
        }

        private void follow() {
            memberService.follow(1L,2L);
            memberService.follow(1L,3L);
            memberService.follow(1L,4L);

            memberService.follow(2L,1L);
            memberService.follow(2L,4L);

            memberService.follow(3L,1L);
            memberService.follow(3L,4L);
            memberService.follow(3L,5L);

            memberService.follow(4L,2L);
            memberService.follow(4L,3L);

            memberService.follow(5L,4L);
        }


        public void createComment(int memberIndex, int postIndex) {
            for (int i=memberIndex; i > 0; i--) {
                for (int j=1; j < postIndex; j++) {
                    commentService.createComment(Long.valueOf(i),Long.valueOf(j),"comment" + j);
                }
            }
        }

        public void createPostLike(int postIndex, int memberIndex) {
            for(int i=1; i < postIndex; i ++) {
                for (int j=1; j < memberIndex; j++) {
                    postService.createPostLike(Long.valueOf(i),Long.valueOf(j));
                }
            }
        }
    }


}
