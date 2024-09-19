package instagram.instagram.service;

import instagram.instagram.domain.hashtag.Hashtag;
import instagram.instagram.domain.hashtag.HashtagRepository;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.domain.post.Post;
import instagram.instagram.domain.post.PostHashtag;
import instagram.instagram.domain.post.PostRepository;
import instagram.instagram.web.dto.post.PostCreateReqDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
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

        Post findPost = postRepository.findById(savedPost.getId()).get();

        assertThat(findPost.getId()).isEqualTo(savedPost.getId()); // postId 테스트
        assertThat(findPost.getMember().getId()).isEqualTo(savedPost.getMember().getId()); // post 의 member 동일 테스트

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
        post.addPostImages(postCreateReqDto.getImageUrls());

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


        Post post = new Post(postCreateReqDto.getContent(), user1);

        if(!postCreateReqDto.getHashtags().isEmpty()) {
            for(String hashtag : postCreateReqDto.getHashtags()) {
                PostHashtag postHashtag = postService.createPostHashtag(post, hashtag);
                post.addPostHashTags(postHashtag);
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

}