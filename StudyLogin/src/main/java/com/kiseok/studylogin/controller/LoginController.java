package com.kiseok.studylogin.controller;

import com.kiseok.studylogin.dto.LoginRequestDto;
import com.kiseok.studylogin.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/login", produces = MediaTypes.HAL_JSON_VALUE)
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    ResponseEntity<?> loginMember(@RequestBody @Valid LoginRequestDto loginRequestDto, Errors errors)   {
        if(errors.hasErrors())  {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return loginService.login(loginRequestDto);
    }

}
