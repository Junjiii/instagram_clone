package instagram.instagram.service;

import instagram.instagram.domain.hashtag.Hashtag;
import instagram.instagram.domain.hashtag.HashtagRepository;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.domain.post.Post;
import instagram.instagram.domain.post.PostRepository;
import instagram.instagram.web.dto.member.MemberJoinReqDto;
import instagram.instagram.web.dto.post.PostCreateReqDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    EntityManager em;


    @Test
    public void 포스트_생성() throws Exception
    {
        // given
        Member user1 = createMemberDto("email@gmamil.com", "user");
        Post savedPost = createPostDto("content", 2, 2, user1.getId());

        em.flush();
        em.clear();

        Post findPost = postRepository.findById(savedPost.getId()).get();

        assertThat(findPost.getId()).isEqualTo(savedPost.getId()); // postId 테스트
        assertThat(findPost.getMember().getId()).isEqualTo(savedPost.getMember().getId()); // post 의 member 동일 테스트

    }


    @Test
    public void 포스트_생성_멤버예외처리() throws Exception
    {
        assertThrows(IllegalArgumentException.class, () -> {
            createPostDto("content", 2, 2,1L);
        });
    }

    @Test
    public void 포스트_생성_여러유저() throws Exception
    {

        Member user1 = createMemberDto("email1@gmamil.com", "user1");
        Member user2 = createMemberDto("email2@gmamil.com", "user2");

        Post savedPost1 = createPostDto("content1", 2, 2, user1.getId());
        Post savedPost2 = createPostDto("content2", 2, 2, user2.getId());


        em.flush();
        em.clear();

        Post findPost1 = postRepository.findById(savedPost1.getId()).get();
        Post findPost2 = postRepository.findById(savedPost2.getId()).get();


        assertThat(findPost1.getId()).isEqualTo(savedPost1.getId()); // postId 테스트
        assertThat(findPost2.getId()).isEqualTo(savedPost2.getId()); // postId 테스트
        assertThat(findPost1.getMember().getId()).isEqualTo(user1.getId()); // post 의 member 동일 테스트
        assertThat(findPost2.getMember().getId()).isEqualTo(user2.getId()); // post 의 member 동일 테스트

        // user1
        assertThat(findPost1.getPostImages().get(0).getImage_URL()).isEqualTo(savedPost1.getPostImages().get(0).getImage_URL());
        assertThat(findPost1.getPostImages().get(0).getSequence()).isEqualTo(1); // 이미지 시퀀스 증가 테스트
        assertThat(findPost1.getPostImages().get(1).getImage_URL()).isEqualTo(savedPost1.getPostImages().get(1).getImage_URL());
        assertThat(findPost1.getPostImages().get(1).getSequence()).isEqualTo(2); // 이미지 시퀀스 증가 테스트

        //user2
        assertThat(findPost2.getPostImages().get(0).getImage_URL()).isEqualTo(savedPost2.getPostImages().get(0).getImage_URL());
        assertThat(findPost2.getPostImages().get(0).getSequence()).isEqualTo(1); // 이미지 시퀀스 증가 테스트
        assertThat(findPost2.getPostImages().get(1).getImage_URL()).isEqualTo(savedPost2.getPostImages().get(1).getImage_URL());
        assertThat(findPost2.getPostImages().get(1).getSequence()).isEqualTo(2); // 이미지 시퀀스 증가 테스트
    }

    @Test
    public void 포스트_이미지_생성() throws Exception
    {
        // given
        Member user1 = createMemberDto("email@gmamil.com", "user");
        Post post = createPostDto("content", 5, 1, user1.getId());


        em.flush();
        em.clear();

        Post findPost = postRepository.findById(post.getId()).get();

        assertThat(findPost.getPostImages().size()).isEqualTo(5);
        assertThat(findPost.getPostImages().get(0).getImage_URL()).isEqualTo(post.getPostImages().get(0).getImage_URL());
        assertThat(findPost.getPostImages().get(0).getSequence()).isEqualTo(1); // 이미지 시퀀스 증가 테스트
        assertThat(findPost.getPostImages().get(1).getImage_URL()).isEqualTo(post.getPostImages().get(1).getImage_URL());
        assertThat(findPost.getPostImages().get(1).getSequence()).isEqualTo(2); // 이미지 시퀀스 증가 테스트
        assertThat(findPost.getPostImages().get(2).getImage_URL()).isEqualTo(post.getPostImages().get(2).getImage_URL());
        assertThat(findPost.getPostImages().get(2).getSequence()).isEqualTo(3); // 이미지 시퀀스 증가 테스트
        assertThat(findPost.getPostImages().get(3).getImage_URL()).isEqualTo(post.getPostImages().get(3).getImage_URL());
        assertThat(findPost.getPostImages().get(3).getSequence()).isEqualTo(4); // 이미지 시퀀스 증가 테스트
        assertThat(findPost.getPostImages().get(4).getImage_URL()).isEqualTo(post.getPostImages().get(4).getImage_URL());
        assertThat(findPost.getPostImages().get(4).getSequence()).isEqualTo(5); // 이미지 시퀀스 증가 테스트
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void 포스트_해쉬태그_생성() throws Exception
    {
        // given
        Member user1 = createMemberDto("email@gmamil.com", "user");

        for (int i = 1; i < 5; i++) {
            hashtagRepository.save(new Hashtag("#Hashtag" + i));
        }


        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag4"); // 이미 등록된 해쉬태그
        hashtags.add("#Hashtag5"); // 새로운 해쉬태그

        PostCreateReqDto postCreateReqDto = new PostCreateReqDto("content1", imageUrls, hashtags);
        Post post = postService.createPost(user1.getId(), postCreateReqDto);


        em.flush();
        em.clear();

        Post findPost = postRepository.findById(post.getId()).get();

        assertThat(findPost.getPostHashtags().size()).isEqualTo(2);
        assertThat(findPost.getPostHashtags().get(0).getHashtag().getHashtag()).isEqualTo(postCreateReqDto.getHashtags().get(0));
        assertThat(findPost.getPostHashtags().get(0).getHashtag().getId()).isEqualTo(4L); // 이미 등록된 해쉬태그 id 테스트
        assertThat(findPost.getPostHashtags().get(1).getHashtag().getHashtag()).isEqualTo(postCreateReqDto.getHashtags().get(1));
        assertThat(findPost.getPostHashtags().get(1).getHashtag().getId()).isEqualTo(5); // 5번째로 등록된 해쉬태그 id 테스트
    }

    @Test
    public void 포스트_라이크_생성() throws Exception
    {
        Member user1 = createMemberDto("email1@gmamil.com", "user1");
        Member user2 = createMemberDto("email2@gmamil.com", "user2");
        Member user3 = createMemberDto("email3@gmamil.com", "user3");
        Member user4 = createMemberDto("email4@gmamil.com", "user4");


        Post post = createPostDto("content", 1, 1, user1.getId());

        postService.createPostLike(post.getId(),user2.getId());
        postService.createPostLike(post.getId(),user3.getId());
        postService.createPostLike(post.getId(),user4.getId());

        em.flush();
        em.clear();

        Post findPost = postRepository.findById(post.getId()).get();

        assertThat(findPost.getPostLikes().size()).isEqualTo(3);
        assertThat(findPost.getPostLikes().get(0).getMember().getName()).isEqualTo(user2.getName());
        assertThat(findPost.getPostLikes().get(1).getMember().getName()).isEqualTo(user3.getName());
        assertThat(findPost.getPostLikes().get(2).getMember().getName()).isEqualTo(user4.getName());


    }

    @Test
    public void 포스트_라이크_여러포스트() throws Exception
    {
        Member user1 = createMemberDto("email1@gmamil.com", "user1");
        Member user2 = createMemberDto("email2@gmamil.com", "user2");
        Member user3 = createMemberDto("email3@gmamil.com", "user3");
        Member user4 = createMemberDto("email4@gmamil.com", "user4");


        Post post1 = createPostDto("content1", 1, 1, user1.getId());
        Post post2 = createPostDto("content2", 1, 1, user2.getId());

        // post1 -> user2, user3 좋아요 누름
        postService.createPostLike(post1.getId(),user2.getId());
        postService.createPostLike(post1.getId(),user3.getId());


        // post2 -> user3 좋아요 누름
        postService.createPostLike(post2.getId(),user3.getId());


        em.flush();
        em.clear();


        Post findPost1 = postRepository.findById(post1.getId()).get();
        Post findPost2 = postRepository.findById(post2.getId()).get();

        assertThat(findPost1.getPostLikes().size()).isEqualTo(2); // post1 좋아요 개수
        assertThat(findPost2.getPostLikes().size()).isEqualTo(1); // post2 좋아요 개수
        assertThat(findPost1.getPostLikes().get(0).getMember().getName()).isEqualTo(user2.getName());
        assertThat(findPost1.getPostLikes().get(1).getMember().getName()).isEqualTo(user3.getName());
        assertThat(findPost2.getPostLikes().get(0).getMember().getName()).isEqualTo(user3.getName());
    }

    public Member createMemberDto(String email, String name) {
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


    public Post createPostDto(String content ,int urlSequence, int hashtagSequence, Long memberId) {
        ArrayList<String> imageUrls = new ArrayList<>();
        ArrayList<String> hashtags = new ArrayList<>();

        for (int i = 1; i < urlSequence+1; i++) {
            imageUrls.add("url" + i);
        }

        for (int i = 1; i < hashtagSequence+1; i++) {
            hashtags.add("#Hashtag" + i);
        }

        PostCreateReqDto postCreateReqDto = new PostCreateReqDto(content, imageUrls, hashtags);
        return postService.createPost(memberId, postCreateReqDto);
    }

}