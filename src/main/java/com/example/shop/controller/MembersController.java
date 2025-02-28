package com.example.shop.controller;


import com.example.shop.dto.MembersDTO;
import com.example.shop.service.MembersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/user")
public class MembersController {

    private final MembersService membersService;



    @GetMapping("/signUp")
    public String signUp (MembersDTO membersDTO) {


        return "user/signUp";

    }

    @PostMapping("/signUp")
    public String signUpPost (@Valid MembersDTO membersDTO , BindingResult bindingResult) {

        log.info("회원가입 포스트 진입 ");
        log.info(membersDTO);

        if (bindingResult.hasErrors()) {

            log.info("유효성검사간 에러 발생~!!!!!");

            log.info(bindingResult.getAllErrors());


            return "user/signUp";

        }



        try {
            membersService.signUp(membersDTO);

        }catch (IllegalStateException e) {
            return "user/signUp";
        }

        return "redirect:/user/signUp";

    }

    @GetMapping("/login")
    public String login () {


        return "user/login";

    }



}
