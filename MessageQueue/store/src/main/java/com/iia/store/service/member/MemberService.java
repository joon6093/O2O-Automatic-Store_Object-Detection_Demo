package com.iia.store.service.member;

import com.iia.store.config.exception.MemberNotFoundException;
import com.iia.store.dto.member.MemberDto;
import com.iia.store.entity.member.Member;
import com.iia.store.repository.member.MemberRepository;
import com.iia.store.service.token.RefreshUserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    private final RefreshUserTokenService refreshUserTokenService;

    public MemberDto read(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return new MemberDto(member.getId(),member.getEmail(), member.getUsername(), member.getNickname(), member.getCreatedAt());
    }

    @Transactional
    @PreAuthorize("@memberGuard.check(#id)")
    public void delete(@Param("id")Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
        deleteRefreshToken(String.valueOf(id));
    }

    public void deleteRefreshToken(String memberId){
        refreshUserTokenService.delete(String.valueOf(memberId));
    }
}