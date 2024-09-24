package instagram.instagram.web.dto.member;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
