package com.iia.store.service.sign;

import com.iia.store.config.exception.*;
import com.iia.store.config.token.TokenHandler;
import com.iia.store.config.token.TokenStorageUtil;
import com.iia.store.dto.sign.*;
import com.iia.store.entity.member.Member;
import com.iia.store.entity.role.Role;
import com.iia.store.entity.role.RoleType;
import com.iia.store.repository.member.MemberRepository;
import com.iia.store.repository.role.RoleRepository;
import com.iia.store.service.token.RefreshUserTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHandler userAccessTokenHandler;
    private final TokenHandler userRefreshTokenHandler;
    private final RefreshUserTokenService refreshUserTokenService;

    public SignService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       @Qualifier("userAccessTokenHandler") TokenHandler userAccessTokenHandler,
                       @Qualifier("userRefreshTokenHandler") TokenHandler userRefreshTokenHandler, RefreshUserTokenService refreshUserTokenService) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAccessTokenHandler = userAccessTokenHandler;
        this.userRefreshTokenHandler = userRefreshTokenHandler;
        this.refreshUserTokenService = refreshUserTokenService;
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        Role role = roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(RoleNotFoundException::new);
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

    @Transactional
    public void signIn(SignInRequest req,  HttpServletResponse response) {
        Member member = memberRepository.findWithRolesByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        validatePassword(req, member);

        TokenHandler.PrivateClaims privateClaims = createPrivateClaims(member);
        String accessToken = userAccessTokenHandler.createToken(privateClaims);
        String refreshToken = userRefreshTokenHandler.createToken(privateClaims);

        saveRefreshToken(String.valueOf(member.getId()), refreshToken);

        TokenStorageUtil.addAccessTokenInHeader(response, accessToken);
        TokenStorageUtil.addRefreshTokenInCookie(response, refreshToken, 24 * 60 * 60);
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

    private void validatePassword(SignInRequest req, Member member) {
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    public void saveRefreshToken(String memberId, String refreshToken) {
        refreshUserTokenService.save(memberId, refreshToken);
    }

    @Transactional
    public void signOut(SignOutRequest req) {
        deleteRefreshToken(String.valueOf(req.getMemberId()));
    }

    public void deleteRefreshToken(String memberId)
    {
        refreshUserTokenService.delete(memberId);
    }

    @Transactional
    public void refresh(String userRefreshToken, HttpServletResponse response) {
        TokenHandler.PrivateClaims userClaims = userRefreshTokenHandler.parse(userRefreshToken)
                .orElseThrow(RefreshTokenFailureException::new);
        if (validateRefreshToken(userRefreshToken, userClaims.getId())) {
            String newUserAccessToken = userAccessTokenHandler.createToken(userClaims);
            String newUserRefreshToken = userRefreshTokenHandler.createToken(userClaims);
            saveRefreshToken(userClaims.getId(), newUserRefreshToken);
            TokenStorageUtil.addAccessTokenInHeader(response, newUserAccessToken);
            TokenStorageUtil.addRefreshTokenInCookie(response, newUserRefreshToken, 24 * 60 * 60);
        }
    }

    public boolean validateRefreshToken(String userRefreshToken, String memberId) {
        return refreshUserTokenService.validate(userRefreshToken, memberId);
    }
}