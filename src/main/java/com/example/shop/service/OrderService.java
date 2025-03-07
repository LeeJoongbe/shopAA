package com.example.shop.service;


import com.example.shop.config.CustomModelMapper;
import com.example.shop.constant.OrderStatus;
import com.example.shop.dto.*;
import com.example.shop.entity.*;
import com.example.shop.exception.OutOfStockException;
import com.example.shop.repository.*;
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
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository  cartItemRepository;


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
                                = new OrderItemDTO(entity , imgEntity.getImgName());
                        orderHistDTO.addOrderItemDTO(orderItemDTO);
                    }
                }
            }
            orderHistDTOList.add(orderHistDTO);
        }
        return  new ResponesPageDTO(requestPageDTO , orderHistDTOList, (int) ordersPage.getTotalElements());
    }


    //주문취소
    public void cancelOrder( Long orderId) {
        //주문취소하려는 주문을 pk로 불러와서
        Orders orders =
        ordersRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        //주문의 주문상태를 취소상태로 변경
        if(orders.getOrderStatus() == OrderStatus.ORDER) {

            orders.setOrderStatus( OrderStatus.CANCEL );
//주문의 주문아이템들의 수량만큼 재고를 더해준다.
            List<OrderItem> orderItemList = orders.getOrderItems();

            for (OrderItem orderItem    : orderItemList) {
//            orderItem.getCount();       //주문수량
//            orderItem.getItem().getStockNumber(); //재고수량

                orderItem.getItem().setStockNumber(
                        orderItem.getItem().getStockNumber()
                                + orderItem.getCount()
                );

            }
        }




    }

    //자신이 주문한 내역인지 확인하는 메소드
    public boolean validateOrder(Long orderId, String email) {
        Members members =
        membersRepository.findByEmail(email);//select * from members
                                             // where email = :email
        Orders orders =
        ordersRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new); //주문목록을 찾아온다.
                                        //select * from orders
                                        //where order_id = :orderId
        Members saveMember =
        orders.getMembers();

        //현재 로그인사용자와 현재 주문의 참조하는회원이 같지 않다면
        if( !members.getEmail().equals( saveMember.getEmail() )) {
            return false;
        }
        return true;

    }

    public void orders (List<Long> cartItemIdList, String email) {
        //참조될 회원찾기

        Members members = membersRepository.findByEmail(email);

        //주문을 만들자

        Orders orders = new Orders();       //주문
        orders.setMembers(members);    //주문이 참조하는 회원
        orders.setOrderStatus(OrderStatus.ORDER);   //주문상태

        List<OrderItem> orderItemList = new ArrayList<>();
        //아이템을 찾기위해서 cartItem을 찾자
        for(Long cartItemId  : cartItemIdList) {
            CartItem cartItem =
            cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

            Item item =
            cartItem.getItem(); //카트아이템이 참조는 아이템

            //주문아이템 entity만들기
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);    //참조하는 아이템
            //주문수량은 카트아이템의 수량
            orderItem.setCount(cartItem.getCount());
            //주문가격
            orderItem.setOrderPrice(item.getPrice());

            //참조하는 주문
            orderItem.setOrders(orders);
            
            //주문을 넣었다면 카트의 아이템은 비워주자
            cartItemRepository.delete(cartItem);

            //주문리스트에 넣어준다.
            orderItemList.add(orderItem);

            //아이템의 재고수량을 변경해야한다.
            // 아이템을 장바구니에 넣었을때는 산게 아니고 아이템을 주문하기를 한다면
            // 기존 재고를 변경해야한다.
            item.orderStockNumber(cartItem.getCount());
            //entity의 값이 변경이 되었기때문에 update 수행

        }
        orders.setOrderItems(orderItemList);

        ordersRepository.save(orders);


    }






}
