package com.kiseok.studylogin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiseok.studylogin.dto.member.MemberModifyRequestDto;
import com.kiseok.studylogin.dto.member.MemberRequestDto;
import com.kiseok.studylogin.dto.member.MemberResponseDto;
import com.kiseok.studylogin.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
    private MemberRepository memberRepository;

    private final String MEMBER_URI = "/api/members";

    @BeforeEach
    void setup()    {
        this.memberRepository.deleteAll();
    }

    @Test
    @DisplayName("정상적으로 유저 생성 -> 201 CREATED")
    void save_member_201() throws Exception {
        MemberRequestDto request = MemberRequestDto.builder()
                .email("kiseok@email.com")
                .password("kiseokPW")
                .build();

        this.mockMvc.perform(post(MEMBER_URI)
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
        this.mockMvc.perform(post(MEMBER_URI)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMemberRequestDto())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()))
        ;

        this.mockMvc.perform(get(MEMBER_URI + "/-1")
            .accept(MediaTypes.HAL_JSON)
            .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }


    @Test
    @DisplayName("정상적으로 유저 불러오기")
    void load_member_200() throws Exception {
        ResultActions actions = this.mockMvc.perform(post(MEMBER_URI)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMemberRequestDto())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()));

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        MemberResponseDto response = objectMapper.readValue(contentAsString, MemberResponseDto.class);

        this.mockMvc.perform(get(MEMBER_URI + "/" + response.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()))
        ;
    }

    @Test
    @DisplayName("DB에 없는 유저 수정하기 -> 404 NOT FOUND")
    void modify_member_404() throws Exception {
        this.mockMvc.perform(post(MEMBER_URI)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMemberRequestDto())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()))
        ;

        this.mockMvc.perform(put(MEMBER_URI + "/-1")
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getAnotherMemberRequestDto())))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("정상적으로 유저 수정하기")
    void modify_member_200() throws Exception {
        ResultActions actions = this.mockMvc.perform(post(MEMBER_URI)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMemberRequestDto())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()));

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        MemberResponseDto response = objectMapper.readValue(contentAsString, MemberResponseDto.class);

        this.mockMvc.perform(put(MEMBER_URI + "/" + response.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getAnotherMemberRequestDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()))
        ;
    }

    @Test
    @DisplayName("DB에 없는 유저 삭제하기 -> 404 NOT FOUND")
    void remove_member_404() throws Exception   {
        this.mockMvc.perform(post(MEMBER_URI)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMemberRequestDto())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()))
        ;

        this.mockMvc.perform(delete(MEMBER_URI + "/-1"))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("정상적으로 유저 삭제하기")
    void remove_member_200() throws Exception   {
        ResultActions actions = this.mockMvc.perform(post(MEMBER_URI)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMemberRequestDto())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(getMemberRequestDto().getEmail()));

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        MemberResponseDto response = objectMapper.readValue(contentAsString, MemberResponseDto.class);

        this.mockMvc.perform(delete(MEMBER_URI + "/" + response.getId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }



    private MemberRequestDto getMemberRequestDto() {
        return MemberRequestDto.builder()
                .email("test@email.com")
                .password("testPW")
                .build();
    }

    private MemberModifyRequestDto getAnotherMemberRequestDto() {
        return MemberModifyRequestDto.builder()
                .password("testAnotherPW")
                .build();
    }

}