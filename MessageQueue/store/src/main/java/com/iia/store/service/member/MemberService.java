package com.iia.store.service.member;

import com.iia.store.config.exception.MemberNotFoundException;
import com.iia.store.dto.member.MemberDto;
import com.iia.store.entity.member.Member;
import com.iia.store.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto read(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return new MemberDto(member.getId(),member.getEmail(), member.getUsername(), member.getNickname(), member.getCreatedAt());
    }

    @Transactional
    public void delete(Long id) { // Todo. refresh token 을 이용한 블랙 리스트 처리 필요
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
    }

}