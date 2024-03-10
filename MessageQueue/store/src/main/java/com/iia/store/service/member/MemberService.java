package com.iia.store.service.member;

import com.iia.store.config.database.RedisHandler;
import com.iia.store.config.exception.MemberNotFoundException;
import com.iia.store.config.exception.ProductNotFoundException;
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

    private final RedisHandler redisHandler;

    public MemberDto read(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return new MemberDto(member.getId(),member.getEmail(), member.getUsername(), member.getNickname(), member.getCreatedAt());
    }

    @Transactional
    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        Boolean exists = redisHandler.exists(String.valueOf(id));
        if (exists != null && exists) {
            redisHandler.deleteValues(String.valueOf(id));
        }
        memberRepository.delete(member);
    }

}