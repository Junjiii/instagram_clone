package instagram.instagram.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinReqDto {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String gender;
    private int year;
    private int month;
    private int day;
}
