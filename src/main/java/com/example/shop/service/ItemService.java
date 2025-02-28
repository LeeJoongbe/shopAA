package com.example.shop.service;


import com.example.shop.dto.ImgDTO;
import com.example.shop.dto.ItemDTO;
import com.example.shop.dto.RequestPageDTO;
import com.example.shop.dto.ResponesPageDTO;
import com.example.shop.entity.ImgEntity;
import com.example.shop.entity.Item;
import com.example.shop.repository.ImgRepository;
import com.example.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    private final ImgService imgService;


    ModelMapper modelMapper = new ModelMapper();

    //등록
    public ItemDTO register(ItemDTO itemDTO, MultipartFile main  , MultipartFile[] multipartFiles) throws IOException {

        Item item = modelMapper.map(itemDTO, Item.class);

//        itemDTO =
//                modelMapper.map(itemRepository.save(item), ItemDTO.class);

        //본문저장
        item = itemRepository.save(item);
        String repimgYn = null;

        imgService.register(main, item.getId(), "Y");


        if(multipartFiles!= null){
            for(int i = 0; i < multipartFiles.length;  i++){
                imgService.register(multipartFiles[i], item.getId(), repimgYn);
            }
        }






        itemDTO = modelMapper.map(item, ItemDTO.class);
        return itemDTO;
    }


    public ResponesPageDTO<ItemDTO> list (String email,  RequestPageDTO requestPageDTO) {

        //정렬조건
        Pageable pageable =
        requestPageDTO.getPageable("id");

        Page<Item> itemPage =
        itemRepository.search(requestPageDTO.getTypes() , requestPageDTO.getKeyword(), email , pageable);

        //Item 변환
        List<Item> itemList = itemPage.getContent();

        List<ItemDTO> list = new ArrayList<>();




        //dto로 변환
        List<ItemDTO> itemDTOList = itemList.stream()
                .map( item -> modelMapper.map(item, ItemDTO.class))
                .collect(Collectors.toList());
        //총 게시글 수
        int total = (int)itemPage.getTotalElements();

        //ResponesPageDTO 타입으로 생성자 생성
        ResponesPageDTO<ItemDTO> responesPageDTO =
                new ResponesPageDTO<>(requestPageDTO, itemDTOList , total);

        return responesPageDTO;

    }

    public ResponesPageDTO<ItemDTO> mainList(RequestPageDTO requestPageDTO){

        Pageable pageable =
        requestPageDTO.getPageable("id");

        Page<Item> itemPage =
        itemRepository.mainList(requestPageDTO.getTypes() , requestPageDTO.getKeyword(), requestPageDTO.getSearchDateType(), pageable);

        //

        //Item 변환
        List<Item> itemList = itemPage.getContent();


        //dto로 변환
//        List<ItemDTO> itemDTOList = itemList.stream()
//                .map( item -> {
//
//                    ItemDTO itemDTO =     modelMapper.map(item, ItemDTO.class);
//                    List<ImgDTO> dtoList = new ArrayList<>();
//
//                    for(ImgEntity img : item.getImgEntityList()){
//                        ImgDTO imgDTO = modelMapper.map(img, ImgDTO.class);
//                        dtoList.add(imgDTO);
//                    }
//
//                    itemDTO.setImgDTOList(dtoList);
//                    return itemDTO;
//                })
//                .collect(Collectors.toList());

        //dto로 변환
        List<ItemDTO> itemDTOList = itemList.stream()
                .map( item -> modelMapper.map(item, ItemDTO.class)
                        .setImgDTOList(item.getImgEntityList().stream().map(
                                imgEntity -> modelMapper.map(imgEntity, ImgDTO.class)
                        ).collect(Collectors.toList())  ))
                .collect(Collectors.toList());
        //총 게시글 수
        int total = (int)itemPage.getTotalElements();

        //ResponesPageDTO 타입으로 생성자 생성
        ResponesPageDTO<ItemDTO> responesPageDTO =
                new ResponesPageDTO<>(requestPageDTO, itemDTOList , total);

        return responesPageDTO;
    }


    public ItemDTO read(Long item_id) {

        Item item =
        itemRepository.findById(item_id).orElseThrow(EntityNotFoundException::new);

        log.info(item);

        List<ImgDTO> imgDTOList =
        imgService.read(item_id);

        ItemDTO itemDTO =
                modelMapper.map(item, ItemDTO.class);

        itemDTO.setImgDTOList(imgDTOList);

        return itemDTO;

    }

    public ItemDTO update(ItemDTO itemDTO , MultipartFile[] multipartFile ,MultipartFile mainimg, Long[] delino ) throws IOException {

        //상품정보수정
        Item item =
        itemRepository.findById(itemDTO.getId()).orElseThrow(EntityNotFoundException::new);

        item.setPrice(itemDTO.getPrice());      //가격수정
        item.setItemNm(itemDTO.getItemNm());    //상품명수정
        item.setItemDetail(itemDTO.getItemDetail());    //상세정보수정
        item.setItemSellStatus(itemDTO.getItemSellStatus());    //판매여부수정
        item.setStockNumber(itemDTO.getStockNumber());          //수량수정
        //상품이미지 삭제 : 배열로 들어온 삭제번호를 이용해서 물리적인 파일삭제  그리고 img db삭제
        if(delino !=null && delino.length > 0) {
            for(Long num  : delino){
                imgService.delImg(num);
            }
        }
        //상품 이미지 등록
        String repimgYn = null;

        //대표이미지
        if( mainimg !=null && !mainimg.isEmpty()){
            imgService.register(mainimg, item.getId(), "Y");
        }

        //상세이미지
        if(multipartFile != null){
            for(int i = 0; i < multipartFile.length;  i++){
                imgService.register(multipartFile[i], item.getId(), repimgYn);
            }
        }


        return modelMapper.map(item, ItemDTO.class);
    }


    public void del(Long item_id){

        List<ImgDTO> imgDTOList =
        imgService.read(item_id);

        //pk를 던져야한다.
        for(ImgDTO imgDTO  : imgDTOList) {
            imgService.delImg(imgDTO.getId());
        }

        //리뷰삭제 추가

        //참조관계인 모든것 추가

        //주문테이블(아이템을 구매한) 삭제된다...

        //


        itemRepository.deleteById(item_id);


    }




}
