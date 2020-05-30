package com.kiseok.studylogin.dto.member;

import lombok.*;
import javax.validation.constraints.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class MemberRequestDto {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotEmpty(message = "이메일은 반드시 입력하여야 합니다.")
    private String email;

    @Size(min = 8, max = 20, message = "비밀번호는 반드시 8자 이상 20자 이하여야 합니다.")
    @NotEmpty(message = "비밀번호는 반드시 입력하여야 합니다.")
    private String password;

}
