package com.example.shop.service;

import com.example.shop.dto.ReplyDTO;
import com.example.shop.dto.RequestPageDTO;
import com.example.shop.dto.ResponesPageDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReplyService {
    //댓글등록
    public ReplyDTO register(ReplyDTO replyDTO , String email);
    
    //댓글 목록
    public ResponesPageDTO<ReplyDTO> listPage(Long bno, RequestPageDTO requestPageDTO );

    public List<ReplyDTO> lll(Pageable pageable);


    //댓글 읽기
    public ReplyDTO read(Long rno);
    
    //댓글 수정
    public ReplyDTO update(ReplyDTO replyDTO, String email);
    
    //댓글 삭제
    public void del(Long rno ,String email);

}
