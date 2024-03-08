package com.iia.store.repository.role;

import com.iia.store.entity.role.Role;
import com.iia.store.entity.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType memberRoleType);
}