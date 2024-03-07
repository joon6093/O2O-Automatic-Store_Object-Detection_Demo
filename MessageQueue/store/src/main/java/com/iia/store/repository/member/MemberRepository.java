package com.iia.store.repository.member;

import com.iia.store.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.roles WHERE m.email = :email")
    Optional<Member> findWithRolesByEmail(@Param("email") String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}