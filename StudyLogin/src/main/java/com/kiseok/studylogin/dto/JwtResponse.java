package com.kiseok.studylogin.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;
}
