package com.iia.store.entity.store;

import com.iia.store.entity.role.Role;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreRoleId implements Serializable {

    private Store store;
    private Role role;

}