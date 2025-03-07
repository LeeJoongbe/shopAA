package com.example.shop.repository;

import com.example.shop.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Query(value = "select * from tbl_board b limit :page, :size, 3, 3", nativeQuery = true)
    public List<Board> pagepagezzz(int page, int size);


    @Query("select b from Board b")
    public Page<Board> pagezzz(Pageable pageable);
    // 제목으로 검색 페이징처리까지
    @Query("select b from Board b where b.title like concat('%', :keyword, '%') ")
    public Page<Board> selectTitle(String keyword, Pageable pageable);

    // 쿼리메소드 제목검색 페이징
    public Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    public Page<Board> findByContentContaining(String keyword, Pageable pageable);

    public Page<Board> findByMembersNameContaining(String keyword, Pageable pageable);

    public Page<Board> findByTitleContainingOrContentContaining(String keyword, String keywordA,Pageable pageable);

    public Page<Board> findByTitleContainingOrMembersNameContaining(String keyword,String keywordA, Pageable pageable);

    @Query("select  b from Board b where b.title like concat('%', :keyword, '%') " +
            " or b.content like concat('%', :keyword, '%')  " +
            " or b.members.name like concat('%', :keyword, '%')   ")
    public Page<Board> selectTCW(String keyword, Pageable pageable);


}
