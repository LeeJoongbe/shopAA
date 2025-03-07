package com.example.shop.entity;


import com.example.shop.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity       //엔티티클래스로 마치 데이터베이스의 테이블과 같은 구조로 작성
//엔티티가 붙은 클래스는 properties의 옵션에 따라 none, update, create 등에
//따라 자동으로 테이블을 생성 할 수도 있음 ,none일경우 테이블을 만들지 않는다. 테이블을 따로 만들어야 함.
@Getter
@Setter
//@ToString(exclude = "board")
@ToString
@NoArgsConstructor
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_num")
})
public class Reply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;   //댓글 pk번호


    @Column(nullable = false)   //not null
    private String replyText;  //댓글 내용


    @ManyToOne(fetch = FetchType.LAZY)  //지연로딩
    @JoinColumn(name = "board_num")
    private Board board;        //참조하는 엔티티 (참조하는 컬럼) fk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_num")
    private Members members;
    //즉시로딩, 지연로딩
    // 즉시로딩 : 연관된 엔티티도 함께 조회 // join

    //지연로딩은 연관된 엔티티를 테이블을 실제로 사용할때 -> 조회
    // 복잡한 많은 엔티티 사용시 상황에 따라 지연로딩


}
