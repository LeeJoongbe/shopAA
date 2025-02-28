package com.example.shop.entity;

import com.example.shop.constant.OrderStatus;
import com.example.shop.entity.base.BaseEntity;
import com.example.shop.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Orders extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //다대일
    @JoinColumn(name = "members_num")
    private Members members;

    //주문상태 //주문  주문취소

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)     //연관관계의 주인인 (자식객체 Foreign Key
                                        //달아준 아이) 테이블에서 참조하는 부모의
                                        //클래스명과 변수명중 변수
    private List<OrderItem> orderItems = new ArrayList<>();





}
