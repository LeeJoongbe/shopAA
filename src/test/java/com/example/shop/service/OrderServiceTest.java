package com.example.shop.service;

import com.example.shop.dto.OrderHistDTO;
import com.example.shop.dto.RequestPageDTO;
import com.example.shop.dto.ResponesPageDTO;
import com.example.shop.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrdersRepository ordersRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void  cancelOrder(){

        Long pk = 1L;

        orderService.cancelOrder(pk);

        log.info(
                ordersRepository.findById(pk)
        );

    }
    @Test
    @Transactional
    public void list(){
        RequestPageDTO requestPageDTO = new RequestPageDTO();

        ResponesPageDTO<OrderHistDTO> responesPageDTO =
        orderService.getOrderList( "test@test.com",requestPageDTO);

        if(responesPageDTO.getDtoList() == null) {
            log.info("주문목록이 없습니다.");
        }else {
            responesPageDTO.getDtoList().forEach(orderHistDTO ->  log.info(orderHistDTO));

            for (int i = 0; i < responesPageDTO.getDtoList().size(); i++) {
                log.info(responesPageDTO.getDtoList().get(i));
            }


        }

    }

}