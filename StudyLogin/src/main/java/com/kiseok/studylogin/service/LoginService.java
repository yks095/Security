package com.kiseok.studylogin.service;

import com.kiseok.studylogin.config.jwt.JwtProvider;
import com.kiseok.studylogin.dto.JwtResponse;
import com.kiseok.studylogin.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {
        if(!authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword()))    {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String token = jwtProvider.generateToken(loginRequestDto.toEntity());

        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.CREATED);
    }

    private boolean authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return true;
        }
        catch (DisabledException | BadCredentialsException e) {
            return false;
        }
    }
}
