package com.kiseok.studylogin.service;

import com.kiseok.studylogin.adapter.MemberAdapter;
import com.kiseok.studylogin.domain.Member;
import com.kiseok.studylogin.dto.member.MemberModifyRequestDto;
import com.kiseok.studylogin.repository.MemberRepository;
import com.kiseok.studylogin.dto.member.MemberRequestDto;
import com.kiseok.studylogin.dto.member.MemberResponseDto;
import com.kiseok.studylogin.enums.MemberRole;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> loadMember(Long id) {
        if(!memberRepository.findById(id).isPresent())  {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MemberResponseDto response = modelMapper.map(this.memberRepository.findById(id).get(), MemberResponseDto.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> saveMember(MemberRequestDto memberRequestDto) {
        if(memberRepository.findByEmail(memberRequestDto.getEmail()).isPresent())   {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Member member = modelMapper.map(memberRequestDto, Member.class);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setMemberRole(MemberRole.USER);
        MemberResponseDto response = modelMapper.map(this.memberRepository.save(member), MemberResponseDto.class);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> modifyMember(Long id, MemberModifyRequestDto request) {
        if(!memberRepository.findById(id).isPresent())  {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Member member = memberRepository.findById(id).get();
        modelMapper.map(request, member);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Cannot Find " + email));
        return new MemberAdapter(member);
    }
}
