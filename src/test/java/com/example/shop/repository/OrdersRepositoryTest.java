package com.example.shop.repository;

import com.example.shop.config.CustomModelMapper;
import com.example.shop.constant.OrderStatus;
import com.example.shop.dto.ImgDTO;
import com.example.shop.dto.ItemDTO;
import com.example.shop.entity.*;
import jakarta.persistence.ManyToOne;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class OrdersRepositoryTest {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    MembersRepository membersRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ImgRepository imgRepository;

    @Autowired
    ModelMapper modelMapper;


    @Test
    public void orderitemInsertTest() {
        //어떤아이템을 샀는가 누구를 참조하는가
       for(int i = 0 ; i < 100000; i ++) {
           Item itemA =
                   itemRepository.findById(133L).get();
           // 누구를 참조하는가
           Orders orders =
                   ordersRepository.findById(1L).get();

           OrderItem item = new OrderItem();
           item.setOrders(orders);     //참조하는 부모엔티티 추가
           item.setItem(itemA);     //참조하는 부모엔티티 추가
           item.setCount(2);
           item.setOrderPrice( itemA.getPrice() * item.getCount()   );

           orderItemRepository.save(item);
       }




    }
    @Test
    public void insertTest() {
        //주문을 등록하려면 부모인 맴버 필요
        Members members =
                membersRepository.findByEmail("test@test.com");

        Orders orders = new Orders();
        orders.setMembers(members);     //참조하는 테이블 추가
        orders.setOrderStatus(OrderStatus.ORDER);

        ordersRepository.save(orders);
    }

    @Test
    @Transactional
    public void readOrdersTest(){
        //양방향 oneToMany
        Orders orders =
        ordersRepository.findById(1L).get();

    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void  cascadeTest(){

        Members members =
                membersRepository.findByEmail("test@test.com");

        Orders orders = new Orders();
        orders.setMembers(members);     //참조하는 테이블 추가
        orders.setOrderStatus(OrderStatus.ORDER);

        //주문테이블에 주문아이템을 같이 가져간다.
        Item itemA =
                itemRepository.findById(133L).get();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrders(orders);
        orderItem.setItem(itemA);
        orderItem.setCount(2);
        orderItem.setOrderPrice( itemA.getPrice() * orderItem.getCount()   );
        //부모가 없다

        List<OrderItem>  orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);       //상품추가


        //부모엔티티에 자식엔티티를 넣어준다.
        orders.setOrderItems(orderItemList);

        //부모만 저장한다.
        ordersRepository.save(orders);



    }


    @Test
    @Transactional
    public void mapperTest(){

        ModelMapper modelMapper = new ModelMapper();
        //modelMapper.map(null, ItemDTO.class);
        ModelMapper modelMapperA = new CustomModelMapper();
//        ItemDTO itemDTO =
//        modelMapperA.map(null, ItemDTO.class);


        ImgEntity imgEntity =
        imgRepository.findById(13L).get();



        log.info(imgEntity);
        ImgDTO imgDTO =
                modelMapper.map(imgEntity, ImgDTO.class);

        log.info(imgDTO);


    }
    @Test
    public void mapperTest2(){



        modelMapper.map(null, ImgDTO.class);



    }


    @Test
    @Transactional
    public void findOrdersTest(){

        Pageable pageable
                = PageRequest.of(0,10);

        Page<Orders> ordersPage =
        ordersRepository.findByMembersEmail("test@test.com",pageable);

        ordersPage.forEach(orders -> log.info(orders));

    }






























}