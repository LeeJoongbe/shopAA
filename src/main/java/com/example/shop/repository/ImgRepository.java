package com.example.shop.repository;

import com.example.shop.entity.ImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImgRepository extends JpaRepository<ImgEntity, Long> {

    public List<ImgEntity> findByItemId (Long item_id);


    //select * from img where item_id= :item_id and repimg_yn =:y

    public ImgEntity findByItemIdAndRepimgYn(Long item_id , String y);
}
