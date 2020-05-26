package com.kiseok.studylogin.dto.member;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class MemberRequestDto {

    private String email;
    private String password;

}
