package com.iia.store.config.security.guard;

import com.iia.store.config.security.PrincipalHandler;
import com.iia.store.entity.role.RoleType;
import com.iia.store.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreGuard extends Guard {

    private final StoreRepository storeRepository;
    private final List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return storeRepository.findById(id)
                .map(store -> store.getMember())
                .map(member -> member.getId())
                .filter(memberId -> memberId.equals(PrincipalHandler.extractId()))
                .isPresent();
    }
}