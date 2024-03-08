package com.iia.store.entity.store;

import com.iia.store.entity.common.EntityDate;
import com.iia.store.entity.member.Member;
import com.iia.store.entity.role.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends EntityDate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 50)
    private String description;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private StoreImage image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL , orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<StoreRole> roles;

    @Builder
    public Store(Long id, String name, String description, Member member, StoreImage image, List<Role> roles) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.member = member;
        this.image = image;
        this.roles = roles.stream().map(r -> new StoreRole(this, r)).collect(toSet());
    }
}
