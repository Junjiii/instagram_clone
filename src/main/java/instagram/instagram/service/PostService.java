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
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    public void createPost(PostCreateReqDto postCreateReqDto) {
        Member member = memberRepository.findById(postCreateReqDto.
                getMemberId()).
                orElseThrow(() -> new IllegalArgumentException("일치하는 팔로워 유저가 없습니다."));

        Post post = new Post(postCreateReqDto.getContent(), member);
        post.addPostImages(postCreateReqDto.getImageUrls());

        if(!postCreateReqDto.getHashtags().isEmpty()) {
            for(String hashtag : postCreateReqDto.getHashtags()) {
                PostHashtag postHashtag = createPostHashtag(post, hashtag);
                post.addPostHashTags(postHashtag);
            }
        }

        postRepository.save(post);
    }

    public PostHashtag createPostHashtag(Post post,String hashtag) {
        Hashtag findHashtag = hashtagRepository.findByHashtag(hashtag);

        PostHashtag.PostHashtagBuilder builder = PostHashtag.builder().post(post);

        if (findHashtag != null) {
            return builder.hashtag(findHashtag).build();
        } else {
            return builder.hashtag(new Hashtag(hashtag)).build();
        }
    }
}
