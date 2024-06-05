package io.elice.shoppingmall.member.service;


import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberRegister;
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

    public Optional<Member> login(MemberLogin loginInfo){
        Optional<Member> member = memberRepository.findByUsername(loginInfo.getUsername());

        if(member.isEmpty())
            return Optional.empty();

        if(!encoder.matches(loginInfo.getPassword(), member.get().getPassword()))
            return Optional.empty();

        return member;
    }

    public void delete(Long id){
        Optional<Member> memberOptional = memberRepository.findById(id);
        memberOptional.ifPresent(memberRepository::delete);
    }

    public Optional<Member> save(MemberRegister memberDto){
        if(memberRepository.existsByUsername(memberDto.getUsername())){
            return Optional.empty();
        }

        memberDto.setPassword(encoder.encode(memberDto.getPassword()));

        return Optional.of(memberRepository.save(memberDto.toUserEntity()));
    }

    public Optional<Member> save(Long id, MemberRegister memberDto){
        Optional<Member> oldMember = memberRepository.findById(id);
        if(oldMember.isEmpty()){
            return save(memberDto);
        }

        Member newMember = memberDto.toUserEntity();
        newMember.setId(id);

        return Optional.of(memberRepository.save(newMember));
    }
}
