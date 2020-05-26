package com.kiseok.studylogin.repository;

import com.kiseok.studylogin.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
