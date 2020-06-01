package com.kiseok.studylogin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiseok.studylogin.config.jwt.JwtProvider;
import com.kiseok.studylogin.dto.LoginRequestDto;
import com.kiseok.studylogin.dto.member.MemberModifyRequestDto;
import com.kiseok.studylogin.dto.member.MemberRequestDto;
import com.kiseok.studylogin.dto.member.MemberResponseDto;
import com.kiseok.studylogin.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.stream.Stream;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberRepository memberRepository;

    private final String MEMBER_URL = "/api/members";

    @AfterEach
    void setup()    {
        this.memberRepository.deleteAll();
    }

    @DisplayName("유저 생성 시 Email 검증 -> 400 BAD_REQUEST")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @MethodSource("validMemberSave")
    void save_member_400(String email, String password) throws Exception {
        MemberRequestDto request = MemberRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("이메일 중복된 유저 생성 -> 400 BAD_REQUEST")
    void save_member_duplicated_400() throws Exception   {
        MemberRequestDto request = MemberRequestDto.builder()
                .email("kiseok@email.com")
                .password("kiseokPW")
                .build();

        this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(request.getEmail()))
        ;

        request.setPassword("duplicatedPW");

        this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("정상적으로 유저 생성 -> 201 CREATED")
    void save_member_201() throws Exception {
        MemberRequestDto request = MemberRequestDto.builder()
                .email("kiseok@email.com")
                .password("kiseokPW")
                .build();

        this.mockMvc.perform(post(MEMBER_URL)
            .accept(MediaTypes.HAL_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(request.getEmail()))
        ;
    }

    @Test
    @DisplayName("DB에 없는 유저 불러오기 -> 404 NOT FOUND")
    void load_member_404() throws Exception {
        MemberRequestDto request = getMemberRequestDto();

        this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()))
        ;

        String token = getToken(request);

        this.mockMvc.perform(get(MEMBER_URL + "/-1")
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("정상적으로 유저 불러오기")
    void load_member_200() throws Exception {
        MemberRequestDto memberRequest = getMemberRequestDto();

        ResultActions actions = this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(memberRequest.getEmail()));

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        MemberResponseDto response = objectMapper.readValue(contentAsString, MemberResponseDto.class);
        String token = getToken(memberRequest);

        this.mockMvc.perform(get(MEMBER_URL + "/" + response.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()))
        ;
    }

    @DisplayName("유저 수정 시 Email 검증 -> 400 BAD_REQUEST")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @MethodSource("validMemberModify")
    void modify_member_400(String password) throws Exception {
        MemberRequestDto memberRequest = getMemberRequestDto();

        ResultActions actions = this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(memberRequest.getEmail()))
        ;

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        MemberResponseDto response = objectMapper.readValue(contentAsString, MemberResponseDto.class);
        String token = getToken(memberRequest);

        MemberModifyRequestDto request = MemberModifyRequestDto.builder()
                .password(password)
                .build();

        this.mockMvc.perform(put(MEMBER_URL + "/" + response.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("DB에 없는 유저 수정하기 -> 404 NOT FOUND")
    void modify_member_404() throws Exception {
        MemberRequestDto memberRequest = getMemberRequestDto();

        this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(memberRequest.getEmail()))
        ;

        String token = getToken(memberRequest);

        this.mockMvc.perform(put(MEMBER_URL + "/-1")
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getAnotherMemberRequestDto()))
                .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("정상적으로 유저 수정하기")
    void modify_member_200() throws Exception {
        MemberRequestDto memberRequest = getMemberRequestDto();

        ResultActions actions = this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(memberRequest.getEmail()));

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        MemberResponseDto response = objectMapper.readValue(contentAsString, MemberResponseDto.class);
        String token = getToken(memberRequest);

        this.mockMvc.perform(put(MEMBER_URL + "/" + response.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getAnotherMemberRequestDto()))
                .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()))
        ;
    }

    @Test
    @DisplayName("DB에 없는 유저 삭제하기 -> 404 NOT FOUND")
    void remove_member_404() throws Exception   {
        MemberRequestDto memberRequest = getMemberRequestDto();

        this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMemberRequestDto())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(memberRequest.getEmail()))
        ;

        String token = getToken(memberRequest);

        this.mockMvc.perform(delete(MEMBER_URL + "/-1")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("정상적으로 유저 삭제하기")
    void remove_member_200() throws Exception   {
        MemberRequestDto memberRequest = getMemberRequestDto();

        ResultActions actions = this.mockMvc.perform(post(MEMBER_URL)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(memberRequest.getEmail()));

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        MemberResponseDto response = objectMapper.readValue(contentAsString, MemberResponseDto.class);
        String token = getToken(memberRequest);

        this.mockMvc.perform(delete(MEMBER_URL + "/" + response.getId())
                .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    private MemberRequestDto getMemberRequestDto() {
        return MemberRequestDto.builder()
                .email("test@email.com")
                .password("testPW123!")
                .build();
    }

    private MemberModifyRequestDto getAnotherMemberRequestDto() {
        return MemberModifyRequestDto.builder()
                .password("testAnotherPW")
                .build();
    }

    private String getToken(MemberRequestDto memberRequest) {
        LoginRequestDto loginRequest = modelMapper.map(memberRequest, LoginRequestDto.class);
        return "Bearer " + jwtProvider.generateToken(loginRequest.toEntity());
    }

    private static Stream<Arguments> validMemberSave()  {
        return Stream.of(
                Arguments.of("", "", true),
                Arguments.of(" ", " ", true),
                Arguments.of("", "password123!", true),
                Arguments.of(" ", "password123!", true),
                Arguments.of("email", "password123!", true),
                Arguments.of("email@", "password123!", true),
                Arguments.of("email@email.", "password123!", true),
                Arguments.of("email@email.com", "", true),
                Arguments.of("email@email.com", " ", true),
                Arguments.of("email@email.com", "pass", true),
                Arguments.of("email@email.com", "passwordpasswordpassword", true)
        );
    }

    private static Stream<Arguments> validMemberModify()  {
        return Stream.of(
                Arguments.of("", true),
                Arguments.of(" ", true),
                Arguments.of("pass", true),
                Arguments.of("passwordpasswordpassword", true)
        );
    }
}