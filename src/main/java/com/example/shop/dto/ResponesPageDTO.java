package com.example.shop.dto;


import lombok.Getter;
import lombok.ToString;

import java.util.List;
@Getter
@ToString
public class ResponesPageDTO <E>{

    // viewpage 보낼때
    // viewpage 사용할
    // 필드값들
    private int page;

    private int size;

    private int total;

    // 시작페이지번호
    private int start;

    // 끝페이지번호
    private int end;

    // 이전페이지 존재여부
    private boolean prev;

    // 다음페이지 존재여부
    private boolean next;

    // 넘겨질 데이터 List
    private List<E> dtoList;

    public ResponesPageDTO(RequestPageDTO requestPageDTO, List<E> dtoList, int total){
        // 사용자로부터 전달받는 requestPageDTO 객체(page, size, keyword, type)
        // 사용자에게 보여질 데이터들, 데이터의 총 수

        if (total <= 0){
            return;
        }
        this.page = requestPageDTO.getPage();
        this.size = requestPageDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end = (int) (Math.ceil(this.page / 10.0)) * 10; //화면의 마지막번호
        this.start = end - 9; // 화면의 첫번째 번호

        int last = (int) ( Math.ceil( (total / (double)size) ) );

        this.end = end > last ? last : end;

        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

    }

}
