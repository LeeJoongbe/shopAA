package com.example.shop.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartItemDTO {
    
    private Long itemId;
    
    @Min(value = 1 , message = "최소 1개 이상 담아주세요")
    private int count;
    
}
