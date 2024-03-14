package com.iia.store.config.security.guard;


import com.iia.store.config.security.PrincipalHandler;
import com.iia.store.entity.role.RoleType;
import java.util.List;

public abstract class Guard {
    public final boolean check(Long id) {
        return hasRole(getRoleTypes()) || isResourceOwner(id);
    }

    abstract protected List<RoleType> getRoleTypes();
    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<RoleType> roleTypes) {
        return PrincipalHandler.extractRoles().containsAll(roleTypes);
    }
}