package com.IIA.o2o_automatic_store_spring.entity.snack;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Snack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String objectName;

    @Embedded
    @Column(nullable = false)
    private Position position;

    @Builder
    public Snack(String filename, String objectName, Position position) {
        this.filename = filename;
        this.objectName = objectName;
        this.position = position;
    }
}
