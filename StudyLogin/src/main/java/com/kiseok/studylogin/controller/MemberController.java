package com.kiseok.studylogin.controller;

import com.kiseok.studylogin.dto.member.MemberModifyRequestDto;
import com.kiseok.studylogin.dto.member.MemberRequestDto;
import com.kiseok.studylogin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_VALUE)
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
    ResponseEntity<?> modifyMember(@PathVariable Long id, @RequestBody MemberModifyRequestDto request) {
        return memberService.modifyMember(id, request);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> removeMember(@PathVariable Long id)   {
        return memberService.removeMember(id);
    }
}
