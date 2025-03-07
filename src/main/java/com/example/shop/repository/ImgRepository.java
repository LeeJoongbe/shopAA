package com.example.shop.repository;

import com.example.shop.entity.ImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImgRepository extends JpaRepository<ImgEntity, Long> {

    public List<ImgEntity> findByItemId (Long item_id);
    public List<ImgEntity> findByBoardNum (Long item_id);


    //select * from img where item_id= :item_id and repimg_yn =:y

    @Query("select i from ImgEntity i where i.board.num = :num")
    public List<ImgEntity> selectBoardnum(Long num);


    public ImgEntity findByItemIdAndRepimgYn(Long item_id , String y);
}
