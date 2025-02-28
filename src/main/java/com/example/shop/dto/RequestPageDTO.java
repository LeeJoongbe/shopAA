package com.example.shop.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Getter @Setter @ToString
@NoArgsConstructor @Builder
@AllArgsConstructor
public class RequestPageDTO {

    // 기본적으로 1인데
    // @Builder.Default 사용해서
    // 파라미터 수집 안될 시 1이 디폴트임
    @Builder.Default
    private int page = 1;

    // 한 페이지에 들어올 게시글 수
    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String searchDateType = "all";

    private String type;

    // 검색의 종류 t, c, w, tc, tw, twc
    // 제목, 내용, 작성자, 제목&내용, 제목&작성자, 제목&내용&작성자
    // Select Box 로 구현
    // if(type.equals("t"))
    // { 만약에 검색의 종류가 제목으로 검색한다면~
    //  select * from table where title = :keyword or
    //  count : keyword }
    private  String keyword;

    // a 태그나 페이지 이동시 url을 쉽게 쓰기위해서
    private String link;

    public String[] getTypes(){
        // 검색을 위한 컬럼을 찾기 위해서 사용
        if(type == null || type.isEmpty()){
        // 현재 type 데이터가 안들어왔다면 파라미터수집을 못했다면 null
            return null;
        }
        // 현재 type tc 라면 split 메소드는 잘라서 배열에 담는 메소드
        // t , c 빈문자열로 잘라서 한단어씩 넣는다.
        return  type.split("");
    }

    // Pageable 페이징처리를 위한 Pageable 만들어준다.
    // ... = 가변 배열임
    public Pageable getPageable(String...props){
        // 컨트롤러에서 파라미터 수집한 page-1과
        // 한페이지에 보여질 게시글 수, 정렬("정렬컬럼"여러개).정렬순서
        return PageRequest
                .of(this.page - 1, this.size, Sort.by(props).descending());
    }

    public String getLink() {

        if (link == null){
            // page=3&size=10 이런 형태로 url 표기됨
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if (type != null && type.length() > 0){
                // 검색시 제목 또는 내용 또는 기타 검색타입을 선택했다면
                // value 값이 있을 것이고 Select Box 최상단에
                // !(-검색타입- value="" 또는 넘어온 데이터가 없다면)
                builder.append("&type=" + type);
            }

            if (keyword != null){
                // 검색 키워드도 수집이 되었다면
                // url 뒤에 &keyword=UTF-8로 인코딩된 검색어 입력
                try {
                  builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
                }catch (UnsupportedEncodingException e){

                }
            }
            // StringBuilder 객체로 만든 url 를 toString 변환
            link = builder.toString();
        }
        System.out.println(link);
        return link;
    }
}
