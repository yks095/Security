package com.kiseok.studylogin.dto.member;

import com.kiseok.studylogin.enums.MemberRole;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MemberResponseDto {

    private Long id;
    private String email;
    private MemberRole memberRole;
}
