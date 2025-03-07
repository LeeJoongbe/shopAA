package com.example.shop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString   @NoArgsConstructor
public class ReplyDTO {

    private Long rno;   //댓글 pk번호

    private Long num;  //본문 번호 // 어디에 달린 댓글인가 ?

    @NotBlank(message = "내용을 작성해라!!!")
    private String replyText;  //댓글 내용


    private String replyer;     //작성자
    private BoardDTO boardDTO;        //참조하는 엔티티 (참조하는 컬럼) fk
    private MembersDTO membersDTO;        //참조하는 엔티티 (참조하는 컬럼) fk

    public ReplyDTO setBoardDTO(BoardDTO boardDTO) {
        this.boardDTO = boardDTO;

        return this;
    }

    private LocalDateTime regDate; // 등록일
    private LocalDateTime modDate; // 수정일

}
