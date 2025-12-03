package com.example.groupbuying.domain.users.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UsersReqDTO {

    @Getter
    @NoArgsConstructor
    public static class SignUpDTO {

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(max = 30, message = "닉네임은 30자 이하여야 합니다.")
        private String nickname;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 50, message = "비밀번호는 8자 이상 50자 이하여야 합니다.")
        private String password;

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Size(max = 20, message = "전화번호는 20자 이하여야 합니다.")
        private String phone;
    }


    @Getter
    @NoArgsConstructor
    public static class LoginDTO {

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 50, message = "비밀번호는 8자 이상 50자 이하여야 합니다.")
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateProfileDTO {

        @Size(max = 30, message = "닉네임은 30자 이하여야 합니다.")
        private String nickname;

        @Size(max = 20, message = "전화번호는 20자 이하여야 합니다.")
        private String phone;
    }
}