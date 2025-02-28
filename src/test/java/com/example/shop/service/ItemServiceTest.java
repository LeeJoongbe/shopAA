package com.example.shop.service;

import com.example.shop.dto.RequestPageDTO;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Log4j2
class ItemServiceTest {

    @Autowired
    ItemService itemService;



    @Test
    @Transactional
    public void test(){

        RequestPageDTO requestPageDTO = new RequestPageDTO();


        itemService.mainList(requestPageDTO);


    }

}