package com.iia.store.config.security.guard;

import com.iia.store.config.security.PrincipalHandler;
import com.iia.store.entity.role.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberGuard extends Guard {
    private final List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return id.equals(PrincipalHandler.extractId());
    }
}