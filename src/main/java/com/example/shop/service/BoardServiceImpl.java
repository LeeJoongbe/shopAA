package com.example.shop.service;

import com.example.shop.dto.BoardDTO;
import com.example.shop.dto.ImgDTO;
import com.example.shop.dto.RequestPageDTO;
import com.example.shop.dto.ResponesPageDTO;
import com.example.shop.entity.Board;
import com.example.shop.repository.BoardRepository;
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
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final ImgService imageService;

    private final FileService fileService; // 파일저장 읽기를 위해서 수정을 위해서
    private ModelMapper modelMapper = new ModelMapper();

    @Override // 등록(파일저장)
    public BoardDTO registerA(BoardDTO boardDTO, MultipartFile multipartFile) throws IOException {
        // DTO로 입력받아서 controller로부터
        // entity로 변환 후 저장하고
        // 저장된 객체를 반환받아 DTO로 변환 후 다시
        // 반환한다. 그래서 controller에서 몇번글이
        // 저장되었는가 등으로 사용할 수 있다.
        // dto > entity / entity>dto 변환은 ModelMapper 로 대체함

        Board board = modelMapper.map(boardDTO , Board.class);

        board = boardRepository.save(board);

        boardDTO = modelMapper.map(board , BoardDTO.class);


        if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
            imageService.register( multipartFile, "board",   board.getNum() , null);
        }

        return boardDTO;
    }



    @Override // 등록
    public BoardDTO register(BoardDTO boardDTO) {
        // DTO로 입력받아서 controller로부터
        // entity로 변환 후 저장하고
        // 저장된 객체를 반환받아 DTO로 변환 후 다시
        // 반환한다. 그래서 controller에서 몇번글이
        // 저장되었는가 등으로 사용할 수 있다.
        // dto > entity / entity>dto 변환은 ModelMapper 로 대체함

        Board board = modelMapper.map(boardDTO , Board.class);

        board = boardRepository.save(board);

        boardDTO = modelMapper.map(board , BoardDTO.class);

        return boardDTO;
    }

    @Override // 목록
    public List<BoardDTO> boardList() {

        // 조회 후 돌려받는 값을 dto로 변환하여 반환한다.
        List<Board> boardList =
            boardRepository.findAll();

        // 반환한다. dto로
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (Board board : boardList){

            BoardDTO boardDTO = modelMapper.map(board , BoardDTO.class);
            boardDTOList.add(boardDTO);

        }

        return boardDTOList;
    }

    @Override
    public void sdfweef(BoardDTO boardDTO) {

        Board board = modelMapper.map(boardDTO, Board.class);

        boardRepository.save(board);


    }

    @Override // 읽기
    public BoardDTO read(Long num) {
        // 넘겨받은 pk값을 가지고 조회 후
        // 데이터를 dto로 변환하여 반환한다.

        Optional<Board> optionalBoard =
                boardRepository.findById(num);

        Board board =
                optionalBoard.get();

        BoardDTO boardDTO =
                modelMapper.map(board , BoardDTO.class);

        // 이미지 추가
        List<ImgDTO> imageDTOList =
            imageService.read(board.getNum() , "board");

        log.info("imageDTOList의 크기로 이미지가 있는지 확인 : " + imageDTOList.size());

        boardDTO.setImageDTOList(imageDTOList);

        return boardDTO;
    }

    @Override
    public BoardDTO update(BoardDTO boardDTO, int[] delimg, MultipartFile multipartFile) throws IOException {
        // 넘겨받은 정보로 데이터 조회 후
        // set으로 수정 수정된 뒤에 dto로 변환 후 반환한다.

        Optional<Board> optionalBoard =
            boardRepository.findById(boardDTO.getNum());

        Board board =
                optionalBoard.get();

        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());

        boardDTO = modelMapper.map(board , BoardDTO.class);

        for (int a : delimg) {
            imageService.delImg( Long.valueOf(a));
        }
        imageService.register(multipartFile ,"board",   board.getNum(), null);

        return boardDTO;
    }

    @Override // 삭제
    public Long del(Long num) {
        // 본문에 포함된 이미지를 먼저 삭제한다.
        // 물리적 파일 삭제, DB삭제
        imageService.delImg(num);


        // 삭제할 번호를 삭제 및 반환
        boardRepository.deleteById(num);



        return num;
    }


    @Override // 페이지 처리
    public List<BoardDTO> boardListPage(Pageable pageable) {

        Page<Board> boardPage =
                boardRepository.pagezzz(pageable);

        List<Board> boardList =
                boardPage.getContent();


        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (Board board : boardList){

            BoardDTO boardDTO = modelMapper.map(board , BoardDTO.class);
            boardDTOList.add(boardDTO);

        }

        return boardDTOList;


    }

    @Override // 페이지 토탈 갯수
    public int boardCount(Pageable pageable) {

        Page<Board> boardPage =
            boardRepository.pagezzz(pageable);

        int count =
        boardPage.getTotalPages();

        return count;
    }

    @Override
    public ResponesPageDTO<BoardDTO> listPage(Long categ_num, RequestPageDTO requestPageDTO) {
        log.info(requestPageDTO);

        // String[] types = requestPageDTO.getTypes();
        // log.info(Arrays.toString(types));
        // log.info(Arrays.toString(types));

        // 페이징 처리된 객체 , 정렬조건 추가
        // page, size는 이미 파라미터 수집으로 값이 있음
        Pageable pageable =
                requestPageDTO.getPageable("num");

        /*Page<Board> boardPage = null;
                // boardRepository.pagezzz(pageable);

        if (requestPageDTO.getType() != null && requestPageDTO.getKeyword() != null){
            // 검색타입이 있고, 검색어가 있다면
            // 타입을 어떤타입인지 t, c, w, tc, tw, tcw > [ 이미 배열로 들어옴 ]

            String keyword = requestPageDTO.getKeyword();

            if (requestPageDTO.getType().equals("t")){
                log.info("제목으로 검색");
                boardPage = boardRepository.selectTitle(requestPageDTO.getKeyword(),pageable);
            }else if (requestPageDTO.getType().equals("w")) {
                log.info("작성자 검색");
                boardPage = boardRepository.findByWriterContaining(keyword, pageable);
            }else if (requestPageDTO.getType().equals("c")){
                log.info("내용으로 검색");
                boardPage = boardRepository.findByContentContaining(keyword, pageable);
            }else if (requestPageDTO.getType().equals("tc")){
                log.info("제목 또는 내용으로 검색");
                boardPage = boardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
            }else if (requestPageDTO.getType().equals("tw")){
                log.info("제목 또는 작성자 검색");
                boardPage = boardRepository.findByTitleContainingOrWriterContaining(keyword, keyword, pageable);
            }else if (requestPageDTO.getType().equals("tcw")){
                log.info("제목 또는 내용 또는 작성자 검색");
                boardPage = boardRepository.selectTCW(keyword, pageable);
            }else{
                log.info("전체리스트 타입이 빈문자열");
                boardPage = boardRepository.pagezzz(pageable);
            }
        }else{
            log.info("전체리스트");
            boardPage = boardRepository.pagezzz(pageable);
        }*/

        // 위처럼 하나한 조건을 if문으로 넣어줬다면
        // SearchImpl 에서 조건을 걸어 사용할 수 있다.

        // 검색처리
        Page<Board> boardPage =
            boardRepository.search(categ_num, requestPageDTO.getTypes(),
                    requestPageDTO.getKeyword(),
                    pageable);




        int total = (int) boardPage.getTotalElements(); // 게시물 총수

        List<Board> boardList =
                boardPage.getContent();

        // dto변환
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board : boardList){

            BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);

            boardDTOList.add(boardDTO);

        }

        ResponesPageDTO<BoardDTO> responesPageDTO =
               new ResponesPageDTO<>(requestPageDTO, boardDTOList, total);

        return responesPageDTO;

    }



}
