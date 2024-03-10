package com.iia.store.service.sign;

import com.iia.store.config.database.RedisHandler;
import com.iia.store.config.exception.*;
import com.iia.store.config.tocken.TokenHandler;
import com.iia.store.dto.sign.*;
import com.iia.store.entity.member.Member;
import com.iia.store.entity.role.Role;
import com.iia.store.entity.role.RoleType;
import com.iia.store.repository.member.MemberRepository;
import com.iia.store.repository.role.RoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHandler userAccessTokenHandler;
    private final TokenHandler userRefreshTokenHandler;
    private final RedisHandler redisHandler;

    public SignService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       @Qualifier("userAccessTokenHandler") TokenHandler userAccessTokenHandler,
                       @Qualifier("userRefreshTokenHandler") TokenHandler userRefreshTokenHandler, RedisHandler redisHandler) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAccessTokenHandler = userAccessTokenHandler;
        this.userRefreshTokenHandler = userRefreshTokenHandler;
        this.redisHandler = redisHandler;
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
    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findWithRolesByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        validatePassword(req, member);
        TokenHandler.PrivateClaims privateClaims = createPrivateClaims(member);
        String accessToken = userAccessTokenHandler.createToken(privateClaims);
        String refreshToken = userRefreshTokenHandler.createToken(privateClaims);
        redisHandler.setValues(String.valueOf(member.getId()), refreshToken);
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

    @Transactional
    public UserRefreshTokenResponse refreshToken(String userRefreshToken) {
        TokenHandler.PrivateClaims userClaims = userRefreshTokenHandler.parse(userRefreshToken).orElseThrow(RefreshTokenFailureException::new);
        Optional.ofNullable(redisHandler.getValues(userClaims.getId()))
                .filter(storedToken -> storedToken.equals(userRefreshToken))
                .orElseThrow(RefreshTokenFailureException::new);

        String newUserAccessToken = userAccessTokenHandler.createToken(userClaims);
        String newUserRefreshToken = userAccessTokenHandler.createToken(userClaims);
        redisHandler.deleteValues(String.valueOf(userClaims.getId()));
        return new UserRefreshTokenResponse(newUserAccessToken, newUserRefreshToken);
    }
}