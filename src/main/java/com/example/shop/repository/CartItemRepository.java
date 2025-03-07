package com.example.shop.repository;

import com.example.shop.dto.CartDetailDTO;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    // 내카트의 아이템과 그 이미지를 가져오는 쿼리
    @Query("select new com.example.shop.dto.CartDetailDTO(ci.id , i.itemNm , i.price , ci.count ,ie.imgName  ) " +
            " from CartItem ci  " +
            "   join Item i         on ci.item.id = i.id " +
            "   join ImgEntity ie   on ie.item.id = i.id " +
            "   where ci.cart.members.email = :email " +
            "       and ie.repimgYn = 'Y' " +
            "       order by ci.id desc")
    public List<CartDetailDTO> findByCartDetailDTOList(String  email );

    public CartItem findByCartIdAndItemId(Long cartId, Long itemId);

}
