package com.example.shop.repository;

import com.example.shop.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    //findAll을 사용시 댓글 전체가 온다.
    //select * from reply;

    //본문에 달린 모든 댓글 가져오기
    // select * from reply where board_num = 1 //1번글에 달린 댓글 전부 가져와
    @Query("select r from Reply r where r.board.num = :bno order by r.rno desc ")
    public List<Reply> listOfBoard(Long bno);
    //페이징처리까지 한 listOfBoard 같은 메소드명 2개 시그니처가 다르다.
    // 파라미터가 위에는 1개 밑에는 2개 // 메소드오버로딩
    @Query("select r from Reply r where r.board.num = :bno")
    public Page<Reply> listOfBoard(Long bno, Pageable pageable);

    //쿼리메소드를 사용한 글번호에 달린 댓글가져오기
    public List<Reply> findByBoardNumOrderByRnoDesc (Long bno);

    public Page<Reply> findByBoardNum (Long bno, Pageable pageable);



}
