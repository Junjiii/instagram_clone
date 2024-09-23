package instagram.instagram.service;

import instagram.instagram.domain.hashtag.Hashtag;
import instagram.instagram.domain.hashtag.HashtagRepository;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.domain.post.Post;
import instagram.instagram.domain.post.PostHashtag;
import instagram.instagram.domain.post.PostRepository;
import instagram.instagram.web.dto.post.PostCreateReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;


    @Transactional
    public Long createPost(Long memberId,PostCreateReqDto postCreateReqDto) {
        Member member = memberRepository.findById(memberId).
                orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));

        Post post = new Post(postCreateReqDto.getContent(), member);

        // image
        if(!postCreateReqDto.getImageUrls().isEmpty()) {
            int sequence = 1;
            for(String imageUrl : postCreateReqDto.getImageUrls()) {
                post.addPostImages(imageUrl,sequence);
                sequence++;
            }
        } else {
            throw new IllegalArgumentException("이미지 1개는 필수 입니다.");
        }


        // hashtag
        if(!postCreateReqDto.getHashtags().isEmpty()) {
            for(String hashtag : postCreateReqDto.getHashtags()) {
                createPostHashtag(post, hashtag);
            }
        }

        postRepository.save(post);
        member.addPosts(post);

        return post.getId();
    }


    public void createPostHashtag(Post post,String hashtag) {
        Hashtag findHashtag = hashtagRepository.findByHashtag(hashtag);

        PostHashtag.PostHashtagBuilder builder = PostHashtag.builder().post(post);

        if (findHashtag != null) {
            post.addPostHashTags(builder.hashtag(findHashtag).build());
        } else {
            Hashtag savedHashtag = hashtagRepository.save(new Hashtag(hashtag));
            post.addPostHashTags(builder.hashtag(savedHashtag).build());
        }
    }


    @Transactional
    public void createPostLike(Long postId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("일치하는 포스트가 없습니다."));

        post.addPostLikes(member);
    }
}
