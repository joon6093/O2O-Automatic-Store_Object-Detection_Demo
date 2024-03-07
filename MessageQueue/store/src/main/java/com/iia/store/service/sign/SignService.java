package com.iia.store.service.sign;

import com.iia.store.config.exception.LoginFailureException;
import com.iia.store.config.exception.MemberEmailAlreadyExistsException;
import com.iia.store.config.exception.MemberNicknameAlreadyExistsException;
import com.iia.store.config.exception.RefreshTokenFailureException;
import com.iia.store.config.exception.RoleNotFoundException;
import com.iia.store.config.tocken.TokenHandler;
import com.iia.store.dto.sign.*;
import com.iia.store.entity.member.Member;
import com.iia.store.entity.member.Role;
import com.iia.store.entity.member.RoleType;
import com.iia.store.repository.member.MemberRepository;
import com.iia.store.repository.role.RoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHandler accountAccessTokenHandler;
    private final TokenHandler accountRefreshTokenHandler;

    public SignService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, @Qualifier("accountAccessTokenHandler") TokenHandler accessTokenHelper, @Qualifier("accountRefreshTokenHandler") TokenHandler refreshTokenHelper) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountAccessTokenHandler = accessTokenHelper;
        this.accountRefreshTokenHandler = refreshTokenHelper;
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        Role role = roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new);
        Member member = Member.builder()
                        .email(req.getEmail())
                        .password(passwordEncoder.encode(req.getPassword()))
                        .username(req.getUsername())
                        .nickname(req.getNickname())
                        .roles(List.of(role))
                        .build();
        memberRepository.save(member);
        return new SignUpResponse(member.getId());
    }

    private void validateSignUpInfo(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        if(memberRepository.existsByNickname(req.getNickname()))
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findWithRolesByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        validatePassword(req, member);
        TokenHandler.PrivateClaims privateClaims = createPrivateClaims(member);
        String accessToken = accountAccessTokenHandler.createToken(privateClaims);
        String refreshToken = accountRefreshTokenHandler.createToken(privateClaims);
        return new SignInResponse(accessToken, refreshToken);
    }

    private void validatePassword(SignInRequest req, Member member) {
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    private TokenHandler.PrivateClaims createPrivateClaims(Member member) {
        return new TokenHandler.PrivateClaims(
                String.valueOf(member.getId()),
                member.getRoles().stream()
                        .map(memberRole -> memberRole.getRole())
                        .map(role -> role.getRoleType())
                        .map(roleType -> roleType.toString())
                        .collect(Collectors.toList()));
    }

    public RefreshTokenResponse refreshToken(String rToken) {
        TokenHandler.PrivateClaims privateClaims = accountRefreshTokenHandler.parse(rToken).orElseThrow(RefreshTokenFailureException::new);
        String accessToken = accountAccessTokenHandler.createToken(privateClaims);
        return new RefreshTokenResponse(accessToken);
    }
}