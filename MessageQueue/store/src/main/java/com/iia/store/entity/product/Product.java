package com.iia.store.entity.product;

import com.iia.store.entity.common.EntityDate;
import com.iia.store.entity.store.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends EntityDate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false)
    private double price;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "quantity_id", nullable = false)
    private Stock stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Store store;

    @Builder
    public Product(Long id, String name, double price, Store store) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = new Stock(0);
        this.store = store;
    }

    public void addImages(List<ProductImage> images) {
        images.forEach(image -> {
            getImages().add(image);
            image.setProduct(this);
        });
    }

    public void removeImages(List<ProductImage> images) {
        images.forEach(image -> {
            getImages().remove(image);
            image.setProduct(null);
        });
    }

    public void updateStock(int quantity) {
        this.stock = new Stock(quantity);
    }
}
