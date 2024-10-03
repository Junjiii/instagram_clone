package instagram.instagram.domain.member.repository;

import instagram.instagram.web.dto.member.MemberProfileDto;
import instagram.instagram.web.dto.member.MemberProfilePostDto;

import java.util.List;

public interface MemberRepositoryCustom {
    MemberProfileDto findMemberProfileDto_withFollowCount(Long id);
    List<MemberProfilePostDto> findPost(Long memberId, int offset, int limit);
}
