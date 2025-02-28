package com.example.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/ex1")
    public String ex1Test(){


        return "sample/ex1";
    }

    @GetMapping("/ex2")
    public String ex2Test(){


        return "sample/ex2";
    }

}
