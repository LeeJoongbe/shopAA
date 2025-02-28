package com.example.shop.entity;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "item")
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //상품코드 pk

    @Column(nullable = false, length = 50)
    private String itemNm;  //상품명

    @Column(name = "price", nullable = false)
    private int price;  //가격

    @Column(nullable = false)
    private int stockNumber;    //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품 상세설명
    
    //상품판매상태      판매중/품절
    @Enumerated(EnumType.STRING)
    ItemSellStatus itemSellStatus;

    @OneToMany(mappedBy = "item" ,fetch = FetchType.LAZY)
    private List<ImgEntity> imgEntityList;//
//    private LocalDateTime regTime;  //상품등록시간
//    private LocalDateTime updateTime;   //상품수정시간




}
