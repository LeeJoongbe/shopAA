package com.example.shop.controller;


import com.example.shop.dto.CartDetailDTO;
import com.example.shop.dto.CartItemDTO;
import com.example.shop.exception.OutOfStockException;
import com.example.shop.service.CartService;
import com.example.shop.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;


    @PostMapping("/cart")
    public ResponseEntity order(@Valid CartItemDTO cartItemDTO,
                                BindingResult bindingResult,
                                Principal principal) {

        log.info(cartItemDTO);
        log.info(cartItemDTO);
        log.info(cartItemDTO);
        log.info(cartItemDTO);
        log.info(cartItemDTO);
        log.info(cartItemDTO);
        log.info(cartItemDTO);

        if (bindingResult.hasErrors()) {
            log.info("장바구니 유효성검사 에러");
            log.info(bindingResult.getAllErrors());

            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

            StringBuilder stringBuilder = new StringBuilder();

            for (FieldError error : fieldErrorList) {
                //StringBuilder객체에  에러 의 메시지를 담는다.
                stringBuilder.append(error.getDefaultMessage());
            }

            //입력된 에러를 다시 보여주기위해 반환값으로 에러내용을 반환해줍니다.
            return new ResponseEntity<String>
                    (stringBuilder.toString(), HttpStatus.BAD_REQUEST);

        }

        //로그아웃이되어서 principal가 null

        if (principal == null) {
            return new ResponseEntity
                    (HttpStatus.UNAUTHORIZED);
        }

        String email = principal.getName();
        Long cartItemId = null;

        try {
            cartItemId =
                    cartService.addCart(cartItemDTO, email);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<String>
                    (e.getMessage(), HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<Long>
                (cartItemId, HttpStatus.OK);


    }


    @GetMapping("/cart")
    public String cartHist(Principal principal, Model model) {

//        List<CartDetailDTO > cartDetailDTOList
//                = cartService.getCartList(principal.getName());
//
//        model.addAttribute("cartDetailDTOList", cartDetailDTOList);

        model.addAttribute("cartDetailDTOList", cartService.getCartList(principal.getName()));


        return "cart/cartList";

    }

    @PatchMapping("/cartItem/{cartItemId}/{count}")
    public ResponseEntity updateCount(
            @PathVariable("cartItemId") Long cartItemId,
            @PathVariable("count") int count, Principal principal) {

        log.info("장바구니아이템번호 :" + cartItemId);
        log.info("수량 : " + count);

        if (count <= 0) {
            return new ResponseEntity<String>("최소1개이상 담아주세요",
                    HttpStatus.BAD_REQUEST);
        } else {

        }
        if (principal == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //현재 카트가 내꺼니?

        if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            //일치하지 않는다면 false값이기때문에 !붙여주고
            //니꺼 아니니까 다시 페이지이동
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        }
        //카트아이템의 수량변경
        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity(HttpStatus.OK);

    }

    //주소 , 방식, 값
    //값을 변수로 받을래? 주소로 받을래?
    @DeleteMapping("/cartItemdel/{cartItemId}")
    public ResponseEntity delcartItem() {

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/cartItemdel")
    public ResponseEntity delcartItem(Long cartItemId, Principal principal) {
        log.info("컨트롤러로 들어온값: " + cartItemId);

        if (principal == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            //일치하지 않는다면 false값이기때문에 !붙여주고
            //니꺼 아니니까 다시 페이지이동
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        }

        //삭제
        try {
            cartService.cartItemdel(cartItemId);

        } catch (EntityNotFoundException e) {

            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/cart/orders")
    public ResponseEntity aaaa(@RequestParam(value = "cartItemIdList", required = false) List<Long> cartItemIdList , Principal principal) {


        if(cartItemIdList == null) {
            return new ResponseEntity<String>( "2" ,HttpStatus.BAD_REQUEST);
        }

        if (principal == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        for (Long cartItemId : cartItemIdList) {
            if (!cartService.validateCartItem(cartItemId, principal.getName())) {
                //일치하지 않는다면 false값이기때문에 !붙여주고
                //니꺼 아니니까 다시 페이지이동
                return new ResponseEntity(HttpStatus.FORBIDDEN);

            }
        }
        //저장  주문
        try {
            orderService.orders(cartItemIdList, principal.getName());

        }catch (OutOfStockException e) {
            log.info("재고 수량 부족");
            return new ResponseEntity<String>( "1" ,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


}




