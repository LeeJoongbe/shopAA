package com.example.shop.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2

class CartServiceTest {

    @Autowired
    CartService cartService;

    @Test
    public void test(){


        cartService.getCartList("test@test.com")
                .forEach(cartDetailDTO -> log.info(cartDetailDTO));

    }


}