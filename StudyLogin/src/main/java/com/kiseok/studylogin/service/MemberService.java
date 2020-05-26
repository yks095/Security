package com.kiseok.studylogin.service;

import com.kiseok.studylogin.domain.Member;
import com.kiseok.studylogin.dto.member.MemberModifyRequest;
import com.kiseok.studylogin.repository.MemberRepository;
import com.kiseok.studylogin.dto.member.MemberRequestDto;
import com.kiseok.studylogin.dto.member.MemberResponseDto;
import com.kiseok.studylogin.enums.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> loadMember(Long id) {
        if(!memberRepository.findById(id).isPresent())  {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MemberResponseDto response = modelMapper.map(this.memberRepository.findById(id).get(), MemberResponseDto.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> saveMember(MemberRequestDto memberRequestDto) {
        Member member = modelMapper.map(memberRequestDto, Member.class);
        member.setMemberRole(MemberRole.USER);
        MemberResponseDto response = modelMapper.map(this.memberRepository.save(member), MemberResponseDto.class);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> modifyMember(Long id, MemberModifyRequest request) {
        if(!memberRepository.findById(id).isPresent())  {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Member member = memberRepository.findById(id).get();
        modelMapper.map(request, member);
        MemberResponseDto response = modelMapper.map(memberRepository.save(member), MemberResponseDto.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> removeMember(Long id) {
        if(!memberRepository.findById(id).isPresent())  {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        memberRepository.delete(memberRepository.findById(id).get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
