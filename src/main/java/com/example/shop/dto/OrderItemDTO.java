package com.example.shop.dto;

import com.example.shop.entity.ImgEntity;
import com.example.shop.entity.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderItemDTO {

    //주문아이템

    private String itemNm;

    private int count;//주문수량

    private int orderPrice; //주문금액

    private String imgUrl;  //상품 이미지 경로

    public OrderItemDTO(OrderItem orderItem, String imgurl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl =imgurl;
    }
}
