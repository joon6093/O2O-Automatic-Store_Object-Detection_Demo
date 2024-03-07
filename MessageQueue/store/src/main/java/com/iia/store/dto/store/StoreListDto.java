package com.iia.store.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StoreListDto {
    private List<StoreSimpleDto> stores;
}
