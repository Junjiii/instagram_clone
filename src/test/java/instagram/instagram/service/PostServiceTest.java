package instagram.instagram.service;

import instagram.instagram.domain.hashtag.Hashtag;
import instagram.instagram.domain.hashtag.HashtagRepository;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.domain.post.Post;
import instagram.instagram.domain.post.PostRepository;
import instagram.instagram.web.dto.post.PostCreateReqDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Rollback
class PostServiceTest {

    @Autowired
    PostService postService;
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
        Member user1 = new Member("user1");
        memberRepository.save(user1);

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");
        imageUrls.add("url2");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag1");
        hashtags.add("#Hashtag2");

        PostCreateReqDto postCreateReqDto = new PostCreateReqDto("content1", imageUrls, hashtags);

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
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");
        imageUrls.add("url2");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag1");
        hashtags.add("#Hashtag2");

        PostCreateReqDto postCreateReqDto = new PostCreateReqDto("content1", imageUrls, hashtags);


        assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(1L, postCreateReqDto);
        });
    }

    @Test
    public void 포스트_생성_여러유저() throws Exception
    {
        Member user1 = new Member("user1");
        Member user2 = new Member("user2");
        memberRepository.save(user1);
        memberRepository.save(user2);

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");
        imageUrls.add("url2");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag1");
        hashtags.add("#Hashtag2");

        PostCreateReqDto postCreateReqDto1 = new PostCreateReqDto("content1", imageUrls, hashtags);
        PostCreateReqDto postCreateReqDto2 = new PostCreateReqDto("content2", imageUrls, hashtags);

        Post savedPost1 = postService.createPost(user1.getId(), postCreateReqDto1);
        Post savedPost2 = postService.createPost(user2.getId(), postCreateReqDto2);

        em.flush();
        em.clear();

        Post findPost1 = postRepository.findById(savedPost1.getId()).get();
        Post findPost2 = postRepository.findById(savedPost2.getId()).get();


        assertThat(findPost1.getId()).isEqualTo(savedPost1.getId()); // postId 테스트
        assertThat(findPost2.getId()).isEqualTo(savedPost2.getId()); // postId 테스트
        assertThat(findPost1.getMember().getId()).isEqualTo(savedPost1.getMember().getId()); // post 의 member 동일 테스트
        assertThat(findPost2.getMember().getId()).isEqualTo(savedPost2.getMember().getId()); // post 의 member 동일 테스트

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
        Member user1 = new Member("user1");

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");
        imageUrls.add("url2");
        PostCreateReqDto postCreateReqDto = new PostCreateReqDto("content1", imageUrls);

        Post post = new Post(postCreateReqDto.getContent(), user1);

        if(!postCreateReqDto.getImageUrls().isEmpty()) {
            int sequence = 1;
            for(String imageUrl : postCreateReqDto.getImageUrls()) {
                post.addPostImages(imageUrl,sequence);
                sequence++;
            }
        }

        memberRepository.save(user1);
        postRepository.save(post);

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
    public void 포스트_해쉬태그_생성() throws Exception
    {
        // given
        Member user1 = new Member("user1");
        memberRepository.save(user1);

        List<Hashtag> all = hashtagRepository.findAll();
        System.out.println("all = " + all.size());


        Hashtag savedHashtag1 = hashtagRepository.save(new Hashtag("#Hashtag1"));
        Hashtag savedHashtag2 = hashtagRepository.save(new Hashtag("#Hashtag2"));
        Hashtag savedHashtag3 = hashtagRepository.save(new Hashtag("#Hashtag3"));
        Hashtag savedHashtag4 = hashtagRepository.save(new Hashtag("#Hashtag4"));
//

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag4"); // 이미 등록된 해쉬태그
        hashtags.add("#Hashtag5"); // 새로운 해쉬태그


        PostCreateReqDto postCreateReqDto = new PostCreateReqDto("content1", imageUrls, hashtags);


        Post post = new Post(postCreateReqDto.getContent(), user1);



        if(!postCreateReqDto.getHashtags().isEmpty()) {
            for(String hashtag : postCreateReqDto.getHashtags()) {
                postService.createPostHashtag(post, hashtag);
            }
        }

        postRepository.save(post);

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
        Member user1 = new Member("user1");
        Member user2 = new Member("user2");
        Member user3 = new Member("user3");
        Member user4 = new Member("user4");

        memberRepository.save(user1);
        memberRepository.save(user2);
        memberRepository.save(user3);
        memberRepository.save(user4);

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag1");

        PostCreateReqDto postCreateReqDto = new PostCreateReqDto("content1", imageUrls, hashtags);

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
        Member user1 = new Member("user1");
        Member user2 = new Member("user2");
        Member user3 = new Member("user3");
        Member user4 = new Member("user4");

        memberRepository.save(user1);
        memberRepository.save(user2);
        memberRepository.save(user3);
        memberRepository.save(user4);

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");

        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#Hashtag1");

        PostCreateReqDto postCreateReqDto1 = new PostCreateReqDto("content1", imageUrls, hashtags);
        PostCreateReqDto postCreateReqDto2 = new PostCreateReqDto("content2", imageUrls, hashtags);

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

}