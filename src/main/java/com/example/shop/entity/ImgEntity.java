package com.example.shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "item")
@NoArgsConstructor
public class ImgEntity {
    //사진을 저장시
    //사진이 달려있는 참조하고 있는 테이블
    //사진이 저장되어있는 경로
    // 경로+이미지의 이름(uuid)
    //사진의 이름 (uuid없는 실제이름)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long id;

    private String imgName; //uuid가 포함된
                            // 파일이름

    private String oriImgName; //짱구라는 이미지이름
    private String imgUrl; //C:\Users\woori\IdeaProjects\demo\demo

    private String repimgYn ;//대표이미지 여부 Y 일경우 대표이미지

    //참조대상
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;




}
