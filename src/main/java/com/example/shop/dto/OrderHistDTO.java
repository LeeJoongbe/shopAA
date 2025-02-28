package com.example.shop.dto;

import com.example.shop.config.CustomModelMapper;
import com.example.shop.constant.OrderStatus;
import com.example.shop.entity.OrderItem;
import com.example.shop.entity.Orders;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderHistDTO {


    private Long orderId ; //주문아이디

    private String orderDate; //주문날짜

    private OrderStatus orderStatus;//주문상태


    //주문 상품 리스트
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    //생성자
    public OrderHistDTO(Orders orders) {
        //기존 생성자는 파라미터로 하나하나 받아서 만드는 기본적인 생성자였으나
        // 객체를 받아서 알맞게 값을 할당해준다.

        this.orderId = orders.getId();
        this.orderDate = orders.getRegTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = orders.getOrderStatus();


        //기존처럼 생성하고 나서 modelMapper로 대체해도 가능하다.


    }




    //메소드
    public void addOrderItemDTO(OrderItemDTO orderItemDTO) {

        orderItemDTOList.add(orderItemDTO);
    }


}
