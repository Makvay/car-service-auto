package ru.car.dto.warehouse;

import lombok.Data;
import java.util.List;

@Data
public class DeductRequest {
    private List<DeductItem> items;
    
    @Data
    public static class DeductItem {
        private Long partId;
        private Integer quantity;
    }
}