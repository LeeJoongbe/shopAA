package com.example.shop.service;


import com.example.shop.config.CustomModelMapper;
import com.example.shop.constant.OrderStatus;
import com.example.shop.dto.*;
import com.example.shop.entity.*;
import com.example.shop.exception.OutOfStockException;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MembersRepository;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final MembersRepository membersRepository;
    private final ItemRepository itemRepository;
    private final         OrderItemRepository orderItemRepository;


    public Long order(OrderDTO orderDTO, String email){
        //참조될 아이템 찾기
        Item item = itemRepository.findById(orderDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        //참조될 회원 찾기
        Members members = membersRepository.findByEmail(email);



        Orders orders = new Orders();
        //부모인 orders set
        orders.setMembers(members);                 //누구의 주문
        orders.setOrderStatus(OrderStatus.ORDER);   //주문상태

        //담을 아이템
        List<OrderItem>  orderItemList = new ArrayList<>();

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);                //입력받은 아이템
        orderItem.setCount(orderDTO.getCount());    //입력받은 주문수량
        orderItem.setOrderPrice(item.getPrice());

        orderItem.setOrders(orders);
        orderItemList.add(orderItem);

        if(item.getStockNumber() - orderDTO.getCount()  < 0) {
            throw new OutOfStockException("상품 재고가 부족합니다. (현재수량 : " +item.getStockNumber() +")" );
        }
        //주문수량만큼 아이템수량 변경
        item.setStockNumber(item.getStockNumber() - orderDTO.getCount()  );


        orders.setOrderItems(orderItemList);

        Orders ordersA =
        ordersRepository.save(orders);


        return ordersA.getId();

    }

    //상품 주문내역
    public ResponesPageDTO getOrderList(String email, RequestPageDTO requestPageDTO) {
        //주문목록
        Page<Orders> ordersPage =
        ordersRepository.findOrders(email, requestPageDTO.getPageable("id"));
        //주문목록 list
        List<Orders> ordersList =
        ordersPage.getContent();
        ModelMapper modelMapper= new CustomModelMapper();
        List<OrderHistDTO> orderHistDTOList1 = ordersList.stream().map( orders -> modelMapper.map( orders , OrderHistDTO.class)  )
                .collect(Collectors.toList());

        //주문목록 dto 변환
        List<OrderHistDTO> orderHistDTOList = new ArrayList<>();
        for(Orders orders  : ordersList) {
            OrderHistDTO orderHistDTO = new OrderHistDTO(orders);       //뷰페이지로 가는 객체 dtoList
            List<OrderItem> orderItemList = orders.getOrderItems();
            for (OrderItem entity :  orderItemList){
                List<ImgEntity> imgEntityList = entity.getItem().getImgEntityList();

                for(ImgEntity imgEntity : imgEntityList){
                    if(imgEntity.getRepimgYn() !=null && imgEntity.getRepimgYn().equals("Y")){
                        OrderItemDTO orderItemDTO
                                = new OrderItemDTO(entity , imgEntity.getImgUrl() + imgEntity.getImgName());
                        orderHistDTO.addOrderItemDTO(orderItemDTO);
                    }
                }
            }
            orderHistDTOList.add(orderHistDTO);
        }
        return  new ResponesPageDTO(requestPageDTO , orderHistDTOList, (int) ordersPage.getTotalElements());
    }






}
