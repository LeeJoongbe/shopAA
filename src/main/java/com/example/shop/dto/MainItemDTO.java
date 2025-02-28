package com.example.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MainItemDTO {
    private Long id;

    private String itemNm;
    private String itemDetail;

    private String imgUrl;      //대표이미지
    private Integer price;      // 가격



}
