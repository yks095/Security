package com.kiseok.studylogin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiseok.studylogin.dto.LoginRequestDto;
import com.kiseok.studylogin.dto.member.MemberRequestDto;
import com.kiseok.studylogin.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.stream.Stream;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    private final String MEMBER_URL = "/api/members";
    private final String LOGIN_URL = "/api/login";

    @AfterEach
    void setUp()    {
        this.memberRepository.deleteAll();
    }

    @DisplayName("로그인 유효성 검사 실패 -> 400 BAD_REQUEST")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @MethodSource("validLogin")
    void login_400(String email, String password) throws Exception   {
        MemberRequestDto memberRequest = MemberRequestDto.builder()
                .email("kiseok@email.com")
                .password("kiseokPW123!")
                .build();

        this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
        ;

        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        this.mockMvc.perform(post(LOGIN_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("비정상적 로그인 -> 404 NOT_FOUND")
    void login_404() throws Exception   {
        LoginRequestDto request = LoginRequestDto.builder()
                .email("test@email.com")
                .password("testPW123!")
                .build();

        this.mockMvc.perform(post(LOGIN_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("정상적으로 로그인 -> 201 CREATED")
    void login_201() throws Exception    {
        MemberRequestDto memberRequest = MemberRequestDto.builder()
                .email("kiseok@email.com")
                .password("kiseokPW123!")
                .build();

        this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
        ;

        LoginRequestDto loginRequest = modelMapper.map(memberRequest, LoginRequestDto.class);
        this.mockMvc.perform(post(LOGIN_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("token").exists())
        ;
    }

    private static Stream<Arguments> validLogin()   {
        return Stream.of(
                Arguments.of("", "kiseokPW123!", true),
                Arguments.of(" ", "kiseokPW123!", true),
                Arguments.of("kiseok@email.com", "", true),
                Arguments.of("kiseok@email.com", " ", true)
        );
    }
}
