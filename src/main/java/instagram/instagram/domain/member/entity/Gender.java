package instagram.instagram.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Gender {
    MALE("male"),FEMALE("female");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    @JsonCreator
    public static Gender of(String genderType) {
        return Arrays.stream(Gender.values())
                .filter(i -> i.gender.equals(genderType))
                .findAny()
                .orElse(null);
    }
}
