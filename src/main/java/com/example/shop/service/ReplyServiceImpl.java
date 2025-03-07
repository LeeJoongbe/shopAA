package com.example.shop.service;


import com.example.shop.dto.*;
import com.example.shop.entity.Board;
import com.example.shop.entity.Members;
import com.example.shop.entity.Reply;
import com.example.shop.repository.BoardRepository;
import com.example.shop.repository.MembersRepository;
import com.example.shop.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MembersRepository membersRepository;
    private ModelMapper modelMapper = new ModelMapper();


    @Override
    public ReplyDTO register(ReplyDTO replyDTO , String email) {
        //dto로 들어온 데이터를 > entity로 변환 저장

        // 댓글내용, 작성자, 날짜는 jpa가 알아서
        // 본문 pk 번호 board의 num이 없다 .
        Members members =
        membersRepository.findByEmail(email);

        //본문pk를 통해서 board객체를 찾아온다.
        Optional<Board> optionalBoard =
                boardRepository.findById(replyDTO.getNum());
        Board board =
                optionalBoard.orElseThrow(EntityNotFoundException::new);
        //찾아온 객체를 reply에 set 해준다.
        log.info("서비스에 들어온 dto : " + replyDTO);
        Reply reply =
                modelMapper.map(replyDTO, Reply.class);
        reply.setBoard(board);
            log.info("저장전 : "  + reply);

        reply.setMembers(members);

        Reply result =
        replyRepository.save(reply);
            log.info("저장후 : " + reply);
//        replyDTO = modelMapper.map(result, ReplyDTO.class);
        //modelMapper을 통해서 데이터를 변환하는 과정간에
        // reply안에 있는 board를 modelmapper을 통해서
        // 다시 boardDTO로 변환해야한다.
        // 변환한 baordDTO를 setBoardDTO를 하면 가능하지만 setBoardDTO는 반환타입이
        // null임으로 replyDTO = 에 해당 데이터를 대입할수 없다.
//        replyDTO = modelMapper.map(result, ReplyDTO.class).setBoardDTO(
//                            modelMapper.map(result.getBoard()  ,  BoardDTO.class)
//                     );

        replyDTO = modelMapper.map(result , ReplyDTO.class);
        replyDTO.setBoardDTO(  modelMapper.map(result.getBoard() , BoardDTO.class) );


        log.info("저장후 데이터변환 DTO : " + replyDTO);
        return replyDTO;
    }

    @Override
    public ResponesPageDTO<ReplyDTO> listPage(Long bno, RequestPageDTO requestPageDTO) {
        //들어온 requestPageDTO를 가지고 
        // 어떤글에 달린 list를 가져오기위해서 글번호 필요
        // 그 안에 있는 pageable을 이용 정렬조건 추가
        // repository 의 검색을 가져온다.
        // 가져와서 dto변환 작업
        // responesPageDTO의 생성자를 이용해서 반환

        Pageable pageable =
        requestPageDTO.getPageable("rno");



        Page<Reply> replyDTOPage =
        replyRepository.findByBoardNum(bno , pageable);

        List<Reply> replyList =
        replyDTOPage.getContent();

        List<ReplyDTO> replyDTOList =
        replyList.stream().map(

                reply -> {
                    ReplyDTO replyDTO = modelMapper.map(reply, ReplyDTO.class);
                    replyDTO.setMembersDTO(  modelMapper.map(reply.getMembers(), MembersDTO.class));

//                    replyDTO.setBoardDTO( modelMapper.map(reply.getBoard(), BoardDTO.class) ) ;

                    return replyDTO;
                }
        ).collect(Collectors.toList());
        //로그
        replyDTOList.forEach( replyDTO ->  log.info(replyDTO));
        //total
        int total =  (int) replyDTOPage.getTotalElements();

        ResponesPageDTO<ReplyDTO> responesPageDTO = new ResponesPageDTO<>(requestPageDTO, replyDTOList, total);

        return responesPageDTO;
    }

    @Override
    public List<ReplyDTO> lll(Pageable pageable) {
        Page<Reply> replyList =
        replyRepository.findAll(pageable);



        return replyList.getContent().stream().map(reply ->  modelMapper.map(reply, ReplyDTO.class) ).collect(Collectors.toList());
    }

    @Override
    public ReplyDTO read(Long rno) {

        // 댓글번호로 댓글찾아와서 dto로 변환
        Optional<Reply> optionalReply =
                replyRepository.findById(rno);

        Reply reply =
                optionalReply.orElseThrow(EntityNotFoundException::new);

        ReplyDTO replyDTO =  modelMapper.map(reply, ReplyDTO.class);
        replyDTO.setMembersDTO(  modelMapper.map(  reply.getMembers() , MembersDTO.class));

        return replyDTO;
    }
    @Override
    public ReplyDTO update(ReplyDTO replyDTO, String  email) {
        //댓글번호로 댓글을 찾아와서 수정
        Optional<Reply> optionalReply =
                replyRepository.findById(replyDTO.getRno());

        Reply reply =
                optionalReply.orElseThrow(EntityNotFoundException::new);
        //작성된 글 변경
        if(reply.getMembers().getEmail().equals(email)) {
            reply.setReplyText(replyDTO.getReplyText());
        }
        return modelMapper.map(reply, ReplyDTO.class);
    }
    @Override
    public void del(Long rno , String  email) {
        //댓글번호로 댓글을 찾아와서
        Optional<Reply> optionalReply =
                replyRepository.findById(rno);
        Reply reply =
                optionalReply.orElseThrow(EntityNotFoundException::new);

        if(email.equals( reply.getMembers().getEmail() )){
            replyRepository.delete(reply);
        }

    }


}
