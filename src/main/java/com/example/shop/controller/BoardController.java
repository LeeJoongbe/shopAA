package com.example.shop.controller;


import com.example.shop.dto.BoardDTO;
import com.example.shop.dto.ReplyDTO;
import com.example.shop.dto.RequestPageDTO;
import com.example.shop.dto.ResponesPageDTO;
import com.example.shop.service.BoardService;
import com.example.shop.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;

    // 등록 get
    @GetMapping("/register")
    public String registerGet(BoardDTO boardDTO , Model model){



        return "board/register";
    }

    // 등록처리 post
    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, MultipartFile imgFile , String name, Model model){
        // @Valid 유효성검사 뒤에다 작성해야함 순서가 있음
        log.info("등록 포스트 진입 : " + boardDTO);
        log.info("등록 포스트 진입 : " + boardDTO);


        if(bindingResult.hasErrors()){ // 유효성검사간 에러가 있다면
            log.info("유효성검사 에러");
            // 어떤 오류가 있는지 콘솔 출력
            log.info(bindingResult.getAllErrors());
            // 현재 메소드의 파라미터로 받은 dto는 그대로 model에 담은것처럼 넘어간다.
            return "board/register";
            // 다시 돌려보낸다.
        }

        // 저장
        try {
            boardDTO =
                    boardService.registerA(boardDTO, imgFile);
        } catch (Exception e) {
            model.addAttribute("msg" , "사진등록이 안됨");
            return "board/register";
        }


        log.info("저장된 데이터 : " + boardDTO);
        log.info("저장된 데이터 : " + boardDTO);
        log.info("저장된 이미지 데이터 : " + imgFile);
        log.info("저장된 이미지 데이터 : " + imgFile);

        return "redirect:/board/listA";
    }


    // 목록
    // 페이지 처리 추가
    // 페이지 토탈 처리
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") int page, RequestPageDTO requestPageDTO){

        log.info(requestPageDTO);
        log.info(requestPageDTO.getLink());
        log.info(requestPageDTO.getPageable());

        Pageable pageable = PageRequest.of(page-1, 10);

        List<BoardDTO> boardDTOList =
                // boardService.boardList();
                boardService.boardListPage(pageable);

        int count =
                boardService.boardCount(pageable);

        model.addAttribute("boardDTOList", boardDTOList);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "board/list";
    }

    // 읽기
    @GetMapping("/read")
    public String read(Long num, Model model, RequestPageDTO requestPageDTO , @RequestParam(value = "pageA", defaultValue = "1") int pageA){

        log.info("read 진입 : " + num);
        log.info(requestPageDTO);

        if (num == null){
            return "redirect:/board/listA";
        }

        BoardDTO boardDTO =
            boardService.read(num);

        log.info(boardDTO);

        model.addAttribute("boardDTO", boardDTO);

        // 본문에 달려있는 이미지가 있는가? 를 찾아야한다
        // 이미지 DB에서 찾아온다. 이미지 경로, 이미지 이름
        // 찾아오는 방법은 fk 이글을 참조하고 있는 row를 이미지테이블에서 찾는다.

        //댓글을 추가
        RequestPageDTO requestPageDTOA = new RequestPageDTO();
        requestPageDTOA.setPage(pageA); //댓글을 위한 requestPageDTO
        ResponesPageDTO<ReplyDTO> replyDTOResponesPageDTO =
        replyService.listPage(num, requestPageDTOA);
        model.addAttribute("replyDTOResponesPageDTO" , replyDTOResponesPageDTO);


        return "board/read";
    }

    // 수정뷰페이지
    @GetMapping("/modify")
    public String modify(Long num, Model model, RequestPageDTO requestPageDTO){
        log.info("modify 진입 : " + num);
        log.info("modify 진입 : " + requestPageDTO);

        if (num == null){
            return "redirect:/board/list";
        }

        BoardDTO boardDTO =
                boardService.read(num);

        model.addAttribute("boardDTO", boardDTO);

        return "board/modify";
    }

    // 수정 처리
    @PostMapping("/modify")
    public String modifyPost(BoardDTO boardDTO, RequestPageDTO requestPageDTO, MultipartFile imgFile,int[] delimg){

        log.info("수정 포스트 진입 : " + boardDTO);
        log.info("수정 포스트 진입 : " + requestPageDTO);
        log.info("수정 포스트 진입 : " + imgFile);

        // 저장 파일이 있다면 저장과 삭제를 동시에 하는 업데이트
        // 삭제할 pk번호 : int[] delimg배열로 삭제
        // 저장 : imgFile

        try {
            boardDTO =
                    boardService.update(boardDTO, delimg, imgFile);

        }catch (IOException e) {

            //사진등록 실패 메시지 추가하여 보내기
            return "redirect:/board/modify";

        }




        log.info("수정된 데이터 출력 : " + boardDTO);
        log.info("수정된 데이터 출력 : " + boardDTO);



        return "redirect:/board/read?num=" + boardDTO.getNum() + '&' + requestPageDTO.getLink();
    }


    // 삭제
    @PostMapping("/del")
    public String delPost(BoardDTO boardDTO){

        log.info("삭제 포스트 진입 : " + boardDTO);

        boardService.del(boardDTO.getNum());

        return "redirect:/board/listA";
    }

    @GetMapping("/listA")
    public String listA(Model model, RequestPageDTO requestPageDTO, Long categ_num){

        log.info(requestPageDTO);
        log.info(requestPageDTO.getLink());
        log.info(requestPageDTO.getPageable());


        ResponesPageDTO responesPageDTO =
                boardService.listPage(categ_num, requestPageDTO);

        log.info(responesPageDTO);
        log.info(responesPageDTO);
        log.info(responesPageDTO);
        log.info(responesPageDTO);

        model.addAttribute("responesPageDTO", responesPageDTO);


        return "board/listA";
    }



}
