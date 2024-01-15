package com.IIA.o2o_automatic_store_spring.entity.snack;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Position {

    private int x1;

    private int x2;

    private int y1;

    private int y2;
}
