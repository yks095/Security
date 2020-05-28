package com.kiseok.studylogin.dto.member;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MemberModifyRequestDto {

    private String password;

}
