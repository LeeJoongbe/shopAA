package com.example.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDTO {


    private  Long itemId;

    @NotNull(message = "수량은 필수 입력값입니다.")
    @Positive(message = "1개 이상 주문해야합니다.")
    private  int count;



}
