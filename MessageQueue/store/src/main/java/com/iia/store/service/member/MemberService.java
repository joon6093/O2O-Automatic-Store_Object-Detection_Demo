package com.iia.store.service.member;

import com.iia.store.dto.member.MemberDto;
import com.iia.store.entity.member.Member;
import com.iia.store.config.exception.MemberNotFoundException;
import com.iia.store.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto read(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .build();
    }

    @Transactional
    public void delete(@Param("id")Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
    }

}