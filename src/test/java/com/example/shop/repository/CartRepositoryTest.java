package com.example.shop.repository;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Members;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Log4j2
class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    MembersRepository membersRepository;


    @Test
    public void  insertTest(){

        //부모인 회원테이블에서 특정회원을 가져와서 set해준다
        // 그걸 통해서 부모를 가지고 있게 된다.
        Members members =
        membersRepository.findByEmail("test@test.com");

//        Members members =
//        membersRepository.findById(3L).get();

        //참조값을 넣으면 참조하는거고 없으면  null들어간다.
        Cart cart= new Cart();
        cart.setMembers(members);
        cartRepository.save(cart);

    }

    @Test
    public void  findByMembersEmail(){

        //부모인 회원테이블에서 특정회원을 가져와서 set해준다
        // 그걸 통해서 부모를 가지고 있게 된다.

        Cart cart =
        cartRepository.selectEmail("test@test.com");

        if(cart == null) {
            log.info("장바구니 만들수 있음");
        }else {
            log.info("장바구니 못만듬");
        }


    }
    @Test
    @Transactional
    public void  findbyid(){

        Cart cart =
        cartRepository.findById(1L).get();

        log.info(cart);

    }


}