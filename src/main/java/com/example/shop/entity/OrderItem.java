package com.example.shop.entity;

import com.example.shop.constant.OrderStatus;
import com.example.shop.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = {"orders"})
@NoArgsConstructor
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //다대일
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)  //다대일
    @JoinColumn(name = "order_id")
    private Orders orders;

    private int orderPrice;  //주문가격

    private int count; //주문수량


}
