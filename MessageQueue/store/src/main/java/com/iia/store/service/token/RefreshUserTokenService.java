package com.iia.store.service.token;

import com.iia.store.config.exception.RefreshUserTokenNotFoundException;
import com.iia.store.entity.token.RefreshUserToken;
import com.iia.store.repository.token.RefreshUserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshUserTokenService {

    private final RefreshUserTokenRepository refreshUserTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(String memberId, String refreshToken) {
        String encryptedRefreshToken = passwordEncoder.encode(refreshToken);
        RefreshUserToken refreshUserToken = refreshUserTokenRepository.findByMemberId(memberId)
                .orElseGet(() -> RefreshUserToken.builder().memberId(memberId).build());
        refreshUserToken.updateToken(encryptedRefreshToken);
        refreshUserTokenRepository.save(refreshUserToken);
    }

    @Transactional
    public void delete(String memberId) {
        RefreshUserToken refreshUserToken = find(memberId);
        refreshUserTokenRepository.delete(refreshUserToken);
    }


    public RefreshUserToken find(String memberId) {
        return refreshUserTokenRepository.findByMemberId(memberId).orElseThrow(RefreshUserTokenNotFoundException::new);
    }

    public boolean validate(String token, String memberId){
        RefreshUserToken storedToken = find(memberId);
        if (!passwordEncoder.matches(token, storedToken.getToken())) {
            delete(memberId);
            return false;
        }
        return true;
    }
}
