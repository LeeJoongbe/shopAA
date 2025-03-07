package com.example.shop.repository;

import com.example.shop.entity.Board;
import com.example.shop.entity.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
// JPQLQuery?
// (Java Persistence Query Language) 객체지향 쿼리로 JPA가 지원하는 다양한 쿼리 방법 중 하나
// ● SQL
// ○ SQL : 테이블을 대상으로 쿼리 ○ JPQL : Entity 객체를 대상으로 쿼리

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

    public BoardSearchImpl() {
        // 어떤 entity를 가지고 만들것이냐? 테이블에 해당하는 entity
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        // 샘플
        // 기능은 쿼리문이 잘 실행되는지 확인
        // 조건은 직접입력 , 파라미터로 받지 않음
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board); // select * from board
        System.out.println("쿼리문 : " + query);
        // select * from board + 조건에 따라서 쿼리문을 작성한다.
        // where title = :파라미터
        // query.where(board.title.contains("1")); // where title like %1%




        query.fetch(); // 구문실행
        System.out.println("총 row(게시글) 수 : " + query.fetchCount());


        return null;
    }

    @Override
    public Page<Board> search(Long categ_num, String[] types, String keyword, Pageable pageable) {

        // select * from board (기본)
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);
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
                if (type.equals("t")){ // 제목
                    booleanBuilder.or(board.title.contains(keyword));
                }else if (type.equals("c")){ // 내용
                    booleanBuilder.or(board.content.contains(keyword));
                }else if (type.equals("w")){ // 작성자
                    booleanBuilder.or(board.members.name.contains(keyword));
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
        query.where(board.num.gt(0L));
        System.out.println("쿼리문 where 추가 : " + query);

        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        // 리스트
        List<Board> boardList =
                query.fetch();
        // 총 게시물
        Long count =
                query.fetchCount();

        return new PageImpl<>(boardList, pageable, count);
    }
}
