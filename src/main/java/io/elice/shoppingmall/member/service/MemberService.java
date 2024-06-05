package io.elice.shoppingmall.member.service;


import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberDTO;
import io.elice.shoppingmall.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

    public Member login(MemberLogin loginInfo){
        Optional<Member> member = memberRepository.findByUsername(loginInfo.getUsername());

        if(member.isEmpty())
            return null;

        if(!encoder.matches(loginInfo.getPassword(), member.get().getPassword()))
            return null;

        return member.get();
    }

    public void delete(Long id){

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
