package com.kiseok.studylogin.dto.member;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MemberModifyRequestDto {

    @Size(min = 8, max = 20, message = "비밀번호는 반드시 8자 이상 20자 이하여야 합니다.")
    @NotBlank(message = "비밀번호는 반드시 입력하여야 합니다.")
    private String password;

}
