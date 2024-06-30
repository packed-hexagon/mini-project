package com.group6.accommodation.domain.auth.model.dto;

import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UserRequestDto {
    
    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;
    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
//    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 영문과 특수문자를 포함하며 8자 이상이어야 합니다.")
    private String password;
    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;
    @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "(01[016789])-\\d{3,4}-\\d{4}", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    public UserEntity toEntity(String encryptedPassword) {
        return UserEntity.builder()
                .email(this.email)
                .encryptedPassword(encryptedPassword)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}
