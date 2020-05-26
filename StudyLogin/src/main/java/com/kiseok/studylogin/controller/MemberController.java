package com.kiseok.studylogin.controller;

import com.kiseok.studylogin.dto.member.MemberModifyRequest;
import com.kiseok.studylogin.dto.member.MemberRequestDto;
import com.kiseok.studylogin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    ResponseEntity<?> loadMember(@PathVariable Long id) {
        return memberService.loadMember(id);
    }

    @PostMapping
    ResponseEntity<?> saveMember(@RequestBody MemberRequestDto requestDto)    {
        return memberService.saveMember(requestDto);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> modifyMember(@PathVariable Long id, @RequestBody MemberModifyRequest request) {
        return memberService.modifyMember(id, request);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> removeMember(@PathVariable Long id)   {
        return memberService.removeMember(id);
    }
}
