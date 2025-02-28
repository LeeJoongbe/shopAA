package com.example.shop.dto;

import com.example.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDTO {

    private Long id;    //상품코드 pk

    @NotBlank(message = "상품명은 필수 입력 값입니다..")
    private String itemNm;  //상품명

    @NotNull(message = "가격은 필수입니다.")
    @PositiveOrZero(message = "가격은 0보다 커야합니다.")
    private int price;  //가격
    
    @NotBlank(message = "상품 상세설명은 필수 입력 값입니다.")
    private String itemDetail;  //상품 상세설명

    @NotNull(message = "재고는 필수입니다.")
    @PositiveOrZero(message = "재고는 0보다 커야합니다.")
    private int stockNumber;    //재고수량

    //상품판매상태      판매중/품절
    ItemSellStatus itemSellStatus;
//
    private LocalDateTime regTime;  //상품등록시간
    private LocalDateTime updateTime;   //상품수정시간

    private String createBy;

    private List<ImgDTO> imgDTOList;

    public ItemDTO setImgDTOList(List<ImgDTO> imgDTOList) {
        this.imgDTOList = imgDTOList;
        return this;
    }
}
