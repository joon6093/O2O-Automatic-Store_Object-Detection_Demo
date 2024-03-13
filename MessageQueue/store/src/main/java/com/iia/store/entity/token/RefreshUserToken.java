package com.iia.store.entity.token;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshUserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberId;

    @Column(nullable = false, unique = true)
    private String token;

    @Builder
    public RefreshUserToken(String memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}