package com.kiseok.studylogin.domain;

import com.kiseok.studylogin.enums.MemberRole;
import lombok.*;
import javax.persistence.*;

@Getter @Setter
@Table @Entity
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
}
