package instagram.instagram.service;

import instagram.instagram.domain.hashtag.Hashtag;
import instagram.instagram.domain.hashtag.HashtagRepository;
import instagram.instagram.domain.member.Member;
import instagram.instagram.domain.member.MemberRepository;
import instagram.instagram.domain.post.Post;
import instagram.instagram.domain.post.PostHashtag;
import instagram.instagram.domain.post.PostQueryRepository;
import instagram.instagram.domain.post.PostRepository;
import instagram.instagram.web.dto.post.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final HashtagRepository hashtagRepository;


    @Transactional
    public Long createPost(Long memberId, PostCreateRequest postCreateRequest) {
        Member member = memberRepository.findById(memberId).
                orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));

        Post post = new Post(postCreateRequest.getContent(), member);

        // image
        if(!postCreateRequest.getImageUrls().isEmpty()) {
            int sequence = 1;
            for(String imageUrl : postCreateRequest.getImageUrls()) {
                post.addPostImages(imageUrl,sequence);
                sequence++;
            }
        } else {
            throw new IllegalArgumentException("이미지 1개는 필수 입니다.");
        }


        // hashtag
        if(!postCreateRequest.getHashtags().isEmpty()) {
            for(String hashtag : postCreateRequest.getHashtags()) {
                createPostHashtag(post, hashtag);
            }
        }

        postRepository.save(post);
        member.addPosts(post);

        return post.getId();
    }

    @Transactional
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


    public PostDetailsDto findPostDetails(Long postId) {
        Post post = postQueryRepository.findPost(postId);
        PostDetailsDto postDetailsDto = new PostDetailsDto(post);

        // postHashtag
        setPostHashtag(post, postDetailsDto);

        // postImages
        setPostImages(post, postDetailsDto);

        // comment
        setPostComment(post, postDetailsDto);

        return postDetailsDto;
    }

    private void setPostComment(Post post, PostDetailsDto postDetailsDto) {
        List<PostCommentDto> postCommentDtos = postQueryRepository.findPostCommentDtos(post.getId());
        postDetailsDto.setPostComments(postCommentDtos);
    }

    private void setPostImages(Post post, PostDetailsDto postDetailsDto) {
        List<PostDetailsImageDto> postDetailsImageDtos = post.getPostImages().stream()
                .map(PostDetailsImageDto::new)
                .collect(Collectors.toList());
        postDetailsDto.setPostDetailsImages(postDetailsImageDtos);
    }

    private void setPostHashtag(Post post, PostDetailsDto postDetailsDto) {
        List<PostHashtagDto> postHashtag = postQueryRepository.findPostHashtags(post.getId());
        postDetailsDto.setPostHashtags(postHashtag);
    }

}
