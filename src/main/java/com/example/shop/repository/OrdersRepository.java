package com.example.shop.repository;

import com.example.shop.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {


    //내가 주문한 주문 목록 : 현재 주문과 주문 아이템은 참조관계이고 , 양방향으로 만들었음
    //그래서 주문을 가져오면 그에 따른 주문아이템도 같이 검색됨 조인!!!
    @Query("select o from Orders o   where o.members.email = :email")
    public Page<Orders> findOrders (String email , Pageable pageable);

    //상동
    public Page<Orders> findByMembersEmail (String email, Pageable pageable);


}
