package com.example.shop.repository;

import com.example.shop.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    public Page<Board> search1(Pageable pageable); // 샘플

    public Page<Board> search(Long categ_num , String[] types, String keyword, Pageable pageable);

}
