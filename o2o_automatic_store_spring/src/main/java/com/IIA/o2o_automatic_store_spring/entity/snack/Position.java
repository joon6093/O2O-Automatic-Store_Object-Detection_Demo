package com.IIA.o2o_automatic_store_spring.entity.snack;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    private int x1;
    private int x2;
    private int y1;
    private int y2;
}
