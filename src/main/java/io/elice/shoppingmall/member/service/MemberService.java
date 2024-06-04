package io.elice.shoppingmall.member.service;


import io.elice.shoppingmall.member.entity.LoginInfo;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberDTO;
import io.elice.shoppingmall.member.repository.MemberRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;


    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id){
        return memberRepository.findById(id);
    }

    public Optional<Member> findByUsername(String username){
        return memberRepository.findByUsername(username);
    }

    public Member login(LoginInfo loginInfo){
        Member member = memberRepository.findByUsername(loginInfo.getUsername()).orElse(null);

        if(member == null)
            return null;

        if(!member.getPassword().equals(loginInfo.getPassword()))
            return null;

        return member;

        /*
        Optional<Member> member = memberRepository.findByUsername(loginInfo.getUsername());

        try {
            if (!member.get().getPassword().equals(loginInfo.getPassword()))
                return null;

            return member;
        } catch (NoSuchElementException e){
            e.printStackTrace();
        }
         */
    }

    public Member save(MemberDTO memberDto){
        try{
            memberDto.setPassword(encoder.encode(memberDto.getPassword()));

            return memberRepository.save(memberDto.toEntity());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Member save(Long id, MemberDTO memberDto){
        Member oldMember = memberRepository.findById(id).orElse(null);
        if(oldMember==null){
            return save(memberDto);
        }

        Member newMember = memberDto.toEntity();
        newMember.setId(id);

        return memberRepository.save(newMember);
    }
}
