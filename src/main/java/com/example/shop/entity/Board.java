package com.example.shop.entity;

import com.example.shop.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@ToString
@Table(name = "tbl_board")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_num")
    private Long num; // 번호

    @Column(length = 50, nullable = false) // 컬럼길이, null 허용여부
    private String title; // 제목



    @Column(length = 2000, nullable = true)
    //nullable = true 디폴트 안써줘도 된다.
    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_num")
    private Members members;

}
