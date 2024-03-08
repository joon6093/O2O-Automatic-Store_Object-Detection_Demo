package com.iia.store.entity.product;

import com.iia.store.entity.common.EntityDate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalog_id")
    private Long id;

    @Column(nullable = false)
    private int quantity;
    @Builder
    public Stock(int quantity) {
        this.quantity = quantity;
    }
}