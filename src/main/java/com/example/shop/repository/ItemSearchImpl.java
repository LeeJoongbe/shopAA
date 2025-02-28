package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.ItemDTO;
import com.example.shop.entity.Item;
import com.example.shop.entity.QImgEntity;
import com.example.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;
// JPQLQuery?
// (Java Persistence Query Language) 객체지향 쿼리로 JPA가 지원하는 다양한 쿼리 방법 중 하나
// ● SQL
// ○ SQL : 테이블을 대상으로 쿼리 ○ JPQL : Entity 객체를 대상으로 쿼리

public class ItemSearchImpl extends QuerydslRepositorySupport implements ItemSearch{

    public ItemSearchImpl() {
        // 어떤 entity를 가지고 만들것이냐? 테이블에 해당하는 entity
        super(Item.class);
    }

    @Override
    public Page<Item> search(String[] types, String keyword,String email, Pageable pageable) {

        // select * from item (기본)
        QItem item = QItem.item;
        JPQLQuery<Item> query = from(item);
        System.out.println("쿼리문 1 : " + query);
        // types에 따른 where문 작성

        if ( (types != null && types.length > 0) && keyword != null ){
            // 검색조건에 따라서 검색 타입이 있고, 검색어가 있다면
            // 조건에따라서 booleanBuilder에 추가
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            // types를 가지고 배열의 값들을 가져와서 조회후(쿼리문작성후) 추가
            // tw > {t,w} > 반복문에서 t 한번 반복, w 한번 반복
            for (String type : types){
                // 만약에 가져온 타입에 따라
                if (type.equals("p")){ // 제목
                    booleanBuilder.or(item.price.gt(Integer.parseInt(keyword)));
                }else if (type.equals("n")){ // 내용
                    booleanBuilder.or(item.itemNm.contains(keyword));
                }else if (type.equals("d")){ // 작성자
                    booleanBuilder.or(item.itemDetail.contains(keyword));
                }else if (type.equals("s")){ // 작성자
                    booleanBuilder.or(item.itemSellStatus.eq(ItemSellStatus.valueOf(keyword)));
                }
            }
            // tw라면
            // select * from board where title = keyword or writer = keyword
            // 위에 만들어진 booleanBuilder 를 가지고 만든다.
            query.where(booleanBuilder);
            // select * from board where title = keyword or writer = keyword
        }
        System.out.println("쿼리문 where 추가 : " + query);
        // 추가적인 쿼리문
        // and num > 0; // 그냥 넣어봄 and 예제임
        query.where(item.id.gt(0L));

        query.where(item.createBy.eq(email));
        System.out.println("쿼리문 where 추가 : " + query);

        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        // 리스트
        List<Item> itemList =
                query.fetch();
        // 총 게시물
        Long count =
                query.fetchCount();

        return new PageImpl<>(itemList, pageable, count);
    }

    @Override
    public Page<Item> mainList(String[] types, String keyword, String searchDateType,  Pageable pageable) {

        QItem item = QItem.item;
        JPQLQuery<Item> query = from(item);

        LocalDateTime time = LocalDateTime.now();

        if( searchDateType.equals("") || searchDateType ==null || searchDateType.equals("all") ){

        }else {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
             if(searchDateType.equals("1d")){
                booleanBuilder.and(item.regTime.after(time.minusDays(1)));
            }else if(searchDateType.equals("1w")){
                booleanBuilder.and(item.regTime.after(time.minusWeeks(1)));
            }else if(searchDateType.equals("1m")){
                booleanBuilder.and(item.regTime.after(time.minusMonths(1)));
            }else if(searchDateType.equals("6m")){
                booleanBuilder.and(item.regTime.after(time.minusMonths(6)));
            }
            query.where(booleanBuilder);

        }

        if ( (types != null && types.length > 0) && keyword != null ){

            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types){
                // 만약에 가져온 타입에 따라
                if (type.equals("p")){ // 가격
                    booleanBuilder.or(item.price.gt(Integer.parseInt(keyword)));
                }else if (type.equals("n")){ // 아이템이름
                    booleanBuilder.or(item.itemNm.contains(keyword));
                }else if (type.equals("d")){ // 아이템상세설명
                    booleanBuilder.or(item.itemDetail.contains(keyword));
                }else if (type.equals("s")){ // 판매여부
                    booleanBuilder.or(item.itemSellStatus.eq(ItemSellStatus.valueOf(keyword)));
                }
            }
            query.where(booleanBuilder);
        }



        query.where(item.id.gt(0L));


        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        // 리스트
        List<Item> itemList =
                query.fetch();
        // 총 게시물
        Long count =
                query.fetchCount();

        return new PageImpl<>(itemList, pageable, count);
    }








}
