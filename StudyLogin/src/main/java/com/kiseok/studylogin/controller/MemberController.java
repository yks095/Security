package com.kiseok.studylogin.controller;

import com.kiseok.studylogin.dto.member.MemberModifyRequestDto;
import com.kiseok.studylogin.dto.member.MemberRequestDto;
import com.kiseok.studylogin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

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
    ResponseEntity<?> saveMember(@RequestBody @Valid MemberRequestDto requestDto, Errors errors)    {
        if(errors.hasErrors())  {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return memberService.saveMember(requestDto);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> modifyMember(@PathVariable Long id, @RequestBody @Valid MemberModifyRequestDto request, Errors errors) {
        if(errors.hasErrors())  {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return memberService.modifyMember(id, request);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> removeMember(@PathVariable Long id)   {
        return memberService.removeMember(id);
    }
}
