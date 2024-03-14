package com.iia.store.config.security.guard;

import com.iia.store.config.security.PrincipalHandler;
import com.iia.store.entity.role.RoleType;
import com.iia.store.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductGuard extends Guard {

    private final ProductRepository productRepository;
    private final List<RoleType> roleTypes = List.of();

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return productRepository.findById(id)
                .map(product -> product.getStore())
                .map(store -> store.getId())
                .filter(storeId -> storeId.equals(PrincipalHandler.extractId()))
                .isPresent();
    }
}