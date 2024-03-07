package com.iia.store;

import com.iia.store.entity.member.Role;
import com.iia.store.entity.member.RoleType;
import com.iia.store.repository.role.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitData {
    private final RoleRepository roleRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initData() {
        initRole();
    }

    private void initRole() {
        roleRepository.saveAll(
                Stream.of(RoleType.values()).map(roleType -> new Role(roleType)).collect(Collectors.toList())
        );
    }
}