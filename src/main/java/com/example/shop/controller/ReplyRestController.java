package com.example.shop.controller;

import com.example.shop.dto.ReplyDTO;
import com.example.shop.dto.RequestPageDTO;
import com.example.shop.dto.ResponesPageDTO;
import com.example.shop.service.ReplyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/reply/rest")
public class ReplyRestController {

    private final ReplyService replyService;


    //저장
    @PostMapping("/register")
    public ResponseEntity register(@Valid ReplyDTO replyDTO, BindingResult bindingResult , Principal principal) {
        log.info("들어오는값 :  " +replyDTO);


        if (principal == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        }

        if(bindingResult.hasErrors()) {
            log.info( bindingResult.getAllErrors() );

            List<ObjectError> list =
                    bindingResult.getAllErrors();

            list.forEach(objectError ->  log.info(objectError));

            return new ResponseEntity< List<ObjectError>  >(list,HttpStatus.BAD_REQUEST);

        }


        ReplyDTO dto =
        replyService.register(replyDTO , principal.getName());

        if(dto == null) {
            return
       new ResponseEntity<String >("저장 실패",HttpStatus.INTERNAL_SERVER_ERROR)  ;
        }else {
            log.info(dto);
            return
       new ResponseEntity<String >(dto.getRno()+ "번글이 저장되었습니다.",HttpStatus.OK)  ;
        }
    }

    @GetMapping("/list")
    public ResponseEntity list(Long num , RequestPageDTO requestPageDTO){

        log.info(num);

        ResponesPageDTO<ReplyDTO> responesPageDTO =
        replyService.listPage(num , requestPageDTO);

        return  new ResponseEntity<ResponesPageDTO>(responesPageDTO, HttpStatus.OK);
    }

    @GetMapping("/read/{rno}")
    public ResponseEntity read(@PathVariable("rno") Long rno){

        log.info(rno);

       try {
           ReplyDTO replyDTO =
                   replyService.read(rno);

           return new ResponseEntity<ReplyDTO>(replyDTO, HttpStatus.OK);

       }catch (EntityNotFoundException e) {


           return new ResponseEntity<String>("게시물이 삭제되었거나 이관되었나 그랬거나 그랬습니다.", HttpStatus.BAD_REQUEST);
       }

    }
    @PostMapping("/update")
    public ResponseEntity update(ReplyDTO replyDTO , Principal principal){

        log.info(replyDTO);

        if(principal == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        }

        try {
            replyDTO =
                    replyService.update(replyDTO,  principal.getName());

            return new ResponseEntity<ReplyDTO>(replyDTO, HttpStatus.OK);

        }catch (EntityNotFoundException e) {


            return new ResponseEntity<String>("게시물이 삭제되었거나 이관되었나 그랬거나 그랬습니다.", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/del")
    public ResponseEntity del(Long rno, Principal principal){
        log.info(rno);
        log.info(rno);
        log.info(rno);
        log.info(rno);
        if(principal == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        }
        try {
                    replyService.del(rno, principal.getName());

            return new ResponseEntity<String>(rno +"번을 삭제하였습니다.", HttpStatus.OK);

        }catch (EntityNotFoundException e) {


            return new ResponseEntity<String>("너님 삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }


}
