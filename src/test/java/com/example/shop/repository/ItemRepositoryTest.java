package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.entity.Item;
import com.example.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.swing.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Log4j2
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext // entitymanager빈을 주입함
    EntityManager em;

    @Test
    public void selectSellerTest(){

        // 상품판매자 이메일
        String email = "a@aaa.aaaa";

        List<Item> itemList =
        itemRepository.findByCreateBy(email);

        itemList.forEach(a -> log.info(a));

    }


    //상품등록 기능을 만들기위한 테스트
    // 상품등록을 잘하는지 읽기 목록 등을 테스트하기위해서
    //더미값 만들기
    @Test
    public void insertTest(){
        //save();   저장
        // entity 저장 , insert into table명(컬럼명,컬럼명,컬럼명) values( 값,값,값  )

      for(int i = 0; i < 100; i++) {
          Item item = new Item();
          item.setItemNm("테스트 상품" +i);
          item.setPrice( 20000  +i);
          item.setItemDetail("이 상품은 머얼리 영국에서부터 시작되어...." +i);
          item.setStockNumber(  100  +i);
          item.setItemSellStatus(ItemSellStatus.SELL);
          item.setRegTime(LocalDateTime.now());
          item.setUpdateTime(LocalDateTime.now());
          // 만들어진 더미객체를
          // (entity로 만들어진 객체임으로 repository를 통해 저장이 가능하다
          Item result =
                  itemRepository.save(item);

          log.info(result);
      }

    }

    @Test
    public void  readTest(){
        //상품을 검색하자
        // 자신의 테이블에 있는 pk번호 사용할것
        // select * from item where item_id = :pk번호;
        // findById(); 동일하다

        Optional<Item> optionalItem =
            itemRepository.findById(1L);

        //예외처리 : 아이템의 pk번호가 없어서 못찾을 경우에 대해서

        try {
            Item item =
            optionalItem.orElseThrow(EntityNotFoundException::new);

            log.info(item);
            log.info(item);
            log.info(item);
            log.info(item);


        }catch (EntityNotFoundException e) {
            log.info("아이템이 존재하지 않습니다.");
            e.printStackTrace();
            log.info("아이템이 존재하지 않습니다.");
            log.info("아이템이 존재하지 않습니다.");
            log.info("아이템이 존재하지 않습니다.");
            log.info("아이템이 존재하지 않습니다.");
            log.info("아이템이 존재하지 않습니다.");
        }
        
        
    }

    @Test
    public void findbyItemNmTest(){

        //상품명으로 검색
        //repository에 만든기능
        //findByItemNm을 테스트
        // == select * from item
        //      where item_nm = :파라미터;
        // == select i from item i
        //      i.itemNm = :파라미터;

        String itemNm = "테스트 상품";

        List<Item> itemList =
        itemRepository.findByItemNm(itemNm);

        itemList.forEach(item -> log.info(item));

    }

    @Test
    public void priceFindTest(){
        //입력한 가격보다 큰 금액의 상품찾기

        int price = 20000;

        List<Item> itemList =
        itemRepository.findByPriceGreaterThanEqual(price);

        itemList.forEach(item ->  log.info(item));


    }

    @Test
    public void findByItemNmOrItemDetailTest(){
        //입력한 상품명 또는 상품설명으로 찾기

       String keyword = "테스트 상품";

        List<Item> itemList =
                itemRepository.findByItemNmOrItemDetail(keyword , keyword);

        itemList.forEach(item ->  log.info(item));


    }

    @Test
    public void findByItemNmContainingOrItemDetailContaining(){
        //입력한 상품명 또는 상품설명으로 찾기

        String keyword = "..";

        List<Item> itemList =
                itemRepository.selectItemNmItemDetail(keyword);
//                itemRepository.selectItemNmItemDetail(keyword , keyword);
//                itemRepository.findByItemNmContainingOrItemDetailContaining(keyword , keyword);

        itemList.forEach(item ->  log.info(item));


    }

    @Test
    @DisplayName("수정테스트")
    public void updateTest(){


        //@Transactional미사용
        // 있는 번호 pk를 지정

//        Item item = new Item();
//        item.setId( 1L );       //지정한 번호를
//        item.setItemNm("상품명입력수정");  //으로 변환한다.
//        item.setPrice(30000);           //으로 변환한다.

        //문제가 생기니 null문제
        // 상품을 먼저 찾아온다.
        Item item =
        itemRepository.findById(1L).get();

        item.setItemNm("상품명입력수정");  //으로 변환한다.
        item.setPrice(50000);           //으로 변환한다.

        log.info("저장하려는 엔티티");
        log.info(item);

        //저장하면 지정한 번호로 item엔티티의 객체의 값으로 수정된다.
//        Item result =
//        itemRepository.save(item);

        //log.info(result);


    }
    
    @Test
    @Transactional
    @Rollback(value = false)
    public void update2Test(){
        //@Transactional 사용

        Item item =
                itemRepository.findById(1L).get();

        item.setItemNm("111111");  //으로 변환한다.
        item.setPrice(50000);           //으로 변환한다.

        log.info("저장하려는 엔티티");
        log.info(item);

        itemRepository.deleteById(item.getId());

    }

    @Test
    public void qdslTest(){

        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);

        // Q도메인을 사용
        QItem qItem = QItem.item;  //select * from item

        //입력받을 검색타입

        String types = "d";  //이름 n과 상세설명d
        String keyword = "영국";
        String[] type = types.split("");
        log.info("type에 담겨있는 검색 타입들");
        log.info(Arrays.toString(type));

        JPQLQuery<Item> itemJPAQuery =
            queryFactory.selectFrom(qItem);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        for ( String typeA  : type){
            if(typeA.equals("n")){
                booleanBuilder.or(qItem.itemNm.like("%" +keyword+"%"));
            }else if(typeA.equals("d")){
                booleanBuilder.or(qItem.itemDetail.like("%" +keyword+"%"));
            }
        }

        log.info(booleanBuilder.toString());
        log.info(booleanBuilder.toString());
        log.info(booleanBuilder.toString());

        itemJPAQuery.where(booleanBuilder );


        List<Item> itemList = itemJPAQuery.fetch(); //실행

        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());
        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());
        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());
        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());
        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());
        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());
        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());
        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());
        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());
        log.info("검색된 게시물수 : " + itemJPAQuery.fetchCount());

        itemList.forEach(item -> log.info(item));



    }




}