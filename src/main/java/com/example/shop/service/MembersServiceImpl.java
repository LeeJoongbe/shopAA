package com.example.shop.service;

import com.example.shop.constant.Role;
import com.example.shop.dto.MembersDTO;
import com.example.shop.entity.Members;
import com.example.shop.repository.MembersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.emitter.Emitable;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService , UserDetailsService {

    private final MembersRepository membersRepository;

    private final PasswordEncoder passwordEncoder;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public String signUp(MembersDTO membersDTO) {

        //이메일로 가입여부 확인
        validateDuplicateMember(membersDTO.getEmail());
        
        //dto를 entity로변환
        Members members = modelMapper.map(membersDTO, Members.class);

        members.setPassword( passwordEncoder.encode(membersDTO.getPassword())  );
        log.info("패스워드 반환값"  +members.getPassword());

        //유저일경우  role을 유저로

        // 어드민일 경우에는 role을 어드민으로 지정
        members.setRole(Role.ADMIN);
//        members.setRole(Role.USER);


        members =
        membersRepository.save(members);

        membersDTO = modelMapper.map(members, MembersDTO.class);

        return membersDTO.getName();
    }












    // 회원가입이 이미 되어있는 여부 확인용

    private void validateDuplicateMember(String email) {
        Members members =
                membersRepository.findByEmail(email);

        if(members != null){
            log.info("이미 가입된 회원임");
            throw  new IllegalStateException("이미 가입된 회원입니다.");
        }else {
            log.info("가입되지 않은 회원임");

        }


    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //입력받은 email을 통해서 entity찾기

        Members members =
        membersRepository.findByEmail(email);

        if(members == null) {
            log.info("현재 입력하신 email로는 가입된 정보가 없습니다.");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다."); //차후에 trycatch처리
        }

        log.info("로그인시도하는사람" + members);

        log.info("로그인하는 사람의 권한" +members.getRole().name());

        //권한을 담을 변수
        String role = "";

        if(members.getRole().name().equals(Role.ADMIN.name())){
            log.info("관리자 로그인시도중");
            //현재 로그인시도하는 사람의 롤을 가져와서 name()메소드로 toString값을
            // role변수에 할당
            role = members.getRole().name();    
        }else {
            log.info("일반유저 로그인 시도중");
            role = members.getRole().name();
        }

        //시큐리티에서 말하는 username는 사용자 이름이 아니라
        //인증을 하는 필드 email 또는 user_id등을 말한다.
        //디비에 있는 password 비밀번호를 UserDetails 객체에 담아서 보내면
        // 호출한 컨트롤러에서 해당 객체를 받아 브라우저에서 입력한 비밀번호와 비교하여 로그인시도
        UserDetails user =
        User.builder().username(members.getEmail()).password(members.getPassword())
                .roles(role).build();

        return user;
    }
}
