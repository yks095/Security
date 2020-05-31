package com.kiseok.studylogin.controller;

import com.kiseok.studylogin.config.jwt.JwtProvider;
import com.kiseok.studylogin.dto.JwtResponse;
import com.kiseok.studylogin.dto.LoginRequestDto;
import com.kiseok.studylogin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @PostMapping
    ResponseEntity<?> loginMember(@RequestBody @Valid LoginRequestDto loginRequestDto) throws Exception {
        authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        String token = jwtProvider.generateToken(loginRequestDto.toEntity());

        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.CREATED);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }
        catch (DisabledException | BadCredentialsException e) {
            throw new Exception("Invalid User!");
        }
    }

}
