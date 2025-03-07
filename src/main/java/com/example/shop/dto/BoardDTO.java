package com.example.shop.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BoardDTO {

    public BoardDTO(Long num, String title, String content) {
        this.num = num;
        this.title = title;
        this.content = content;
    }

    private Long num; // 번호

    //@NotNull // 널X
    //@NotEmpty // 널X "" 빈문자열X
    //@NotBlank // 널X " " 스페이스 공백X
    @NotBlank(message = "제목은 필수로 작성해주세요.")
    @Size(min = 3, max = 50, message = "최소 3글자 최대 50글자로 작성해주세요.")
    private String title; // 제목

    @NotBlank(message = "잘써라")
    @Size(min = 2, max = 2000, message = "2~2000까지 작성가능")
    private String content; // 내용

    @NotBlank(message = "작성자 꼭 꺼야함")
    @Size(min = 1, max = 20, message = "20자 이내로 작성")
    private String writer; // 작성자

    // 이미지
    List<ImgDTO> imageDTOList = new ArrayList<>();

    private Long cate_num;

    private LocalDateTime regDate; // 등록일자
    private LocalDateTime modDate; // 수정일자

}
