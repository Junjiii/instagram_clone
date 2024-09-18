package instagram.instagram.web.dto.post;

import lombok.Getter;

import java.util.List;

@Getter
public class PostCreateReqDto {

    private String content;
    private Long memberId;
    private List<String> imageUrls;
    private List<String> hashtags;


    public PostCreateReqDto(String content, Long memberId, List<String> imageUrls) {
        this.content = content;
        this.memberId = memberId;
        this.imageUrls = imageUrls;
    }

    public PostCreateReqDto(String content, Long memberId, List<String> imageUrls, List<String> hashtags) {
        this.content = content;
        this.memberId = memberId;
        this.imageUrls = imageUrls;
        this.hashtags = hashtags;
    }
}
