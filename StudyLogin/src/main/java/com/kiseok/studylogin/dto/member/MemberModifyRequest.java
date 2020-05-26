package com.kiseok.studylogin.dto.member;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MemberModifyRequest {

    private String password;

}
