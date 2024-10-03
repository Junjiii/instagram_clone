package instagram.instagram.web.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberJoinRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String gender;
    private int year;
    private int month;
    private int day;

    @QueryProjection
    public MemberJoinRequest(String email, String password, String name, String phoneNumber, String gender, int year, int month, int day) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
