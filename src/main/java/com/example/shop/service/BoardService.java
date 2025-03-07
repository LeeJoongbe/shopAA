package com.example.shop.service;

import com.example.shop.dto.BoardDTO;
import com.example.shop.dto.RequestPageDTO;
import com.example.shop.dto.ResponesPageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    // 등록 : 등록한 글을 반환 받는다. DTO로
    public BoardDTO register(BoardDTO boardDTO);
    public BoardDTO registerA(BoardDTO boardDTO, MultipartFile multipartFile) throws IOException;

    // 목록
    public List<BoardDTO> boardList();

    public void sdfweef(BoardDTO boardDTO);

    // 페이지
    public List<BoardDTO> boardListPage(Pageable pageable);
    public int boardCount(Pageable pageable);

    // ResponsePageDTO 를 사용한 객체
    public ResponesPageDTO<BoardDTO> listPage(Long categ_num , RequestPageDTO requestPageDTO);


    //공지사항


    // 읽기
    public BoardDTO read(Long num);

    // 수정
    public BoardDTO update(BoardDTO boardDTO,int[] delimg, MultipartFile multipartFile) throws IOException;

    // 삭제
    public Long del(Long num);


}
