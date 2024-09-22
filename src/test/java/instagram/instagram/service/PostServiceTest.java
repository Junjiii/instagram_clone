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
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

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
        MemberJoinReqDto memberJoinReqDto = createDto("email@gmamil.com", "user");
        Member user1 = memberService.join(memberJoinReqDto);

        PostCreateReqDto postCreateReqDto = createPostDto("content", 2, 2);

        Post savedPost = postService.createPost(user1.getId(), postCreateReqDto);

        em.flush();
        em.clear();

        Post findPost = postRepository.findById(savedPost.getId()).get();

        assertThat(findPost.getId()).isEqualTo(savedPost.getId()); // postId 테스트
        assertThat(findPost.getMember().getId()).isEqualTo(savedPost.getMember().getId()); // post 의 member 동일 테스트

    }


    @Test
    public void 포스트_생성_멤버예외처리() throws Exception
    {
        PostCreateReqDto postCreateReqDto = createPostDto("content", 2, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(1L, postCreateReqDto);
        });
    }

    @Test
    public void 포스트_생성_여러유저() throws Exception
    {
        MemberJoinReqDto memberJoinReqDto1 = createDto("email1@gmamil.com", "user1");
        MemberJoinReqDto memberJoinReqDto2 = createDto("email2@gmamil.com", "user2");
        Member user1 = memberService.join(memberJoinReqDto1);
        Member user2 = memberService.join(memberJoinReqDto2);

        PostCreateReqDto postCreateReqDto1 = createPostDto("content1", 2, 2);
        PostCreateReqDto postCreateReqDto2 = createPostDto("content2", 2, 2);

        Post savedPost1 = postService.createPost(user1.getId(), postCreateReqDto1);
        Post savedPost2 = postService.createPost(user2.getId(), postCreateReqDto2);

        em.flush();
        em.clear();

        Post findPost1 = postRepository.findById(savedPost1.getId()).get();
        Post findPost2 = postRepository.findById(savedPost2.getId()).get();


        assertThat(findPost1.getId()).isEqualTo(savedPost1.getId()); // postId 테스트
        assertThat(findPost2.getId()).isEqualTo(savedPost2.getId()); // postId 테스트
        assertThat(findPost1.getMember().getId()).isEqualTo(user1.getId()); // post 의 member 동일 테스트
        assertThat(findPost2.getMember().getId()).isEqualTo(user2.getId()); // post 의 member 동일 테스트

        // user1
        assertThat(findPost1.getPostImages().get(0).getImage_URL()).isEqualTo(postCreateReqDto1.getImageUrls().get(0));
        assertThat(findPost1.getPostImages().get(0).getSequence()).isEqualTo(1); // 이미지 시퀀스 증가 테스트
        assertThat(findPost1.getPostImages().get(1).getImage_URL()).isEqualTo(postCreateReqDto1.getImageUrls().get(1));
        assertThat(findPost1.getPostImages().get(1).getSequence()).isEqualTo(2); // 이미지 시퀀스 증가 테스트

        //user2
        assertThat(findPost2.getPostImages().get(0).getImage_URL()).isEqualTo(postCreateReqDto2.getImageUrls().get(0));
        assertThat(findPost2.getPostImages().get(0).getSequence()).isEqualTo(1); // 이미지 시퀀스 증가 테스트
        assertThat(findPost2.getPostImages().get(1).getImage_URL()).isEqualTo(postCreateReqDto2.getImageUrls().get(1));
        assertThat(findPost2.getPostImages().get(1).getSequence()).isEqualTo(2); // 이미지 시퀀스 증가 테스트
    }

    @Test
    public void 포스트_이미지_생성() throws Exception
    {
        // given
        MemberJoinReqDto memberJoinReqDto = createDto("email@gmamil.com", "user");
        Member user1 = memberService.join(memberJoinReqDto);

        PostCreateReqDto postCreateReqDto = createPostDto("content", 2, 2);
        Post post = postService.createPost(user1.getId(), postCreateReqDto);

        em.flush();
        em.clear();

        Post findPost = postRepository.findById(post.getId()).get();

        assertThat(findPost.getPostImages().size()).isEqualTo(2);
        assertThat(findPost.getPostImages().get(0).getImage_URL()).isEqualTo(postCreateReqDto.getImageUrls().get(0));
        assertThat(findPost.getPostImages().get(0).getSequence()).isEqualTo(1); // 이미지 시퀀스 증가 테스트
        assertThat(findPost.getPostImages().get(1).getImage_URL()).isEqualTo(postCreateReqDto.getImageUrls().get(1));
        assertThat(findPost.getPostImages().get(1).getSequence()).isEqualTo(2); // 이미지 시퀀스 증가 테스트
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void 포스트_해쉬태그_생성() throws Exception
    {
        // given
        MemberJoinReqDto memberJoinReqDto = createDto("email@gmamil.com", "user");
        Member user1 = memberService.join(memberJoinReqDto);

        Hashtag savedHashtag1 = hashtagRepository.save(new Hashtag("#Hashtag1"));
        Hashtag savedHashtag2 = hashtagRepository.save(new Hashtag("#Hashtag2"));
        Hashtag savedHashtag3 = hashtagRepository.save(new Hashtag("#Hashtag3"));
        Hashtag savedHashtag4 = hashtagRepository.save(new Hashtag("#Hashtag4"));


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
        assertThat(findPost.getPostHashtags().get(0).getHashtag().getId()).isEqualTo(savedHashtag4.getId()); // 이미 등록된 해쉬태그 id 테스트
        assertThat(findPost.getPostHashtags().get(1).getHashtag().getHashtag()).isEqualTo(postCreateReqDto.getHashtags().get(1));
        assertThat(findPost.getPostHashtags().get(1).getHashtag().getId()).isEqualTo(5); // 5번째로 등록된 해쉬태그 id 테스트
    }

    @Test
    public void 포스트_라이크_생성() throws Exception
    {
        MemberJoinReqDto memberJoinReqDto1 = createDto("email1@gmail.com", "user1");
        MemberJoinReqDto memberJoinReqDto2 = createDto("email2@gmail.com", "user2");
        MemberJoinReqDto memberJoinReqDto3 = createDto("email3@gmail.com", "user3");
        MemberJoinReqDto memberJoinReqDto4 = createDto("email4@gmail.com", "user4");

        Member user1 = memberService.join(memberJoinReqDto1);
        Member user2 = memberService.join(memberJoinReqDto2);
        Member user3 = memberService.join(memberJoinReqDto3);
        Member user4 = memberService.join(memberJoinReqDto4);


        PostCreateReqDto postCreateReqDto = createPostDto("content", 1, 1);
        Post post = postService.createPost(user1.getId(), postCreateReqDto);

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
        MemberJoinReqDto memberJoinReqDto1 = createDto("email1@gmail.com", "user1");
        MemberJoinReqDto memberJoinReqDto2 = createDto("email2@gmail.com", "user2");
        MemberJoinReqDto memberJoinReqDto3 = createDto("email3@gmail.com", "user3");
        MemberJoinReqDto memberJoinReqDto4 = createDto("email4@gmail.com", "user4");

        Member user1 = memberService.join(memberJoinReqDto1);
        Member user2 = memberService.join(memberJoinReqDto2);
        Member user3 = memberService.join(memberJoinReqDto3);
        Member user4 = memberService.join(memberJoinReqDto4);

        PostCreateReqDto postCreateReqDto1 = createPostDto("content1", 2, 2);
        PostCreateReqDto postCreateReqDto2 = createPostDto("content2", 2, 2);

        Post post1 = postService.createPost(user1.getId(), postCreateReqDto1);
        Post post2 = postService.createPost(user2.getId(), postCreateReqDto1);

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

    public MemberJoinReqDto createDto(String email, String name) {
        return new MemberJoinReqDto(
                email,
                "password",
                name,
                "010-1111-1111",
                "male",
                2024,
                11,
                11
        );
    }


    public PostCreateReqDto createPostDto(String content ,int urlSequence, int hashtagSequence) {
        ArrayList<String> imageUrls = new ArrayList<>();
        ArrayList<String> hashtags = new ArrayList<>();

        for (int i = 1; i < urlSequence+1; i++) {
            imageUrls.add("url" + i);
        }

        for (int i = 1; i < hashtagSequence+1; i++) {
            hashtags.add("#Hashtag" + i);
        }

        return new PostCreateReqDto(content, imageUrls, hashtags);
    }

}