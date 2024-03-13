package com.iia.store.repository.token;

import com.iia.store.entity.store.Store;
import com.iia.store.entity.token.RefreshUserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshUserTokenRepository extends JpaRepository<RefreshUserToken, Long> {
    Optional<RefreshUserToken> findByMemberId(String memberId);

}
