package io.elice.shoppingmall.member.service;


import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberModifyInfo;
import io.elice.shoppingmall.member.entity.MemberRegister;
import io.elice.shoppingmall.member.entity.MemberResponseDTO;
import io.elice.shoppingmall.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
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
        Optional<Member> memberOptional = memberRepository.findByUsername(loginInfo.getUsername());

        if(memberOptional.isEmpty())
            return Optional.empty();

        if(!encoder.matches(loginInfo.getPassword(), memberOptional.get().getPassword()))
            return Optional.empty();

        return memberOptional;
    }

    public void delete(Long id){
        Optional<Member> memberOptional = memberRepository.findById(id);
        memberOptional.ifPresent(memberRepository::delete);
    }

    public boolean isExistUsername(String username){
        return memberRepository.existsByUsername(username);
    }

    public boolean isMatchPassword(Long id, String password){
        Optional<Member> memberOptional = findById(id);

        if(memberOptional.isEmpty())
            return false;

        if(!encoder.matches(password, memberOptional.get().getPassword()))
            return false;

        return true;
    }

    public Optional<MemberResponseDTO> save(MemberRegister memberDto){
        memberDto.setPassword(encoder.encode(memberDto.getPassword()));
        Member member = memberRepository.save(memberDto.toUserEntity());

        return Optional.of(new MemberResponseDTO(member));
    }

    public Optional<MemberResponseDTO> save(Long id, MemberModifyInfo memberModifyInfo){
        Optional<Member> oldMemberOptional = findById(id);
        if(oldMemberOptional.isEmpty()){
            return Optional.empty();
        }

        memberModifyInfo.setModifyPassword(encoder.encode(memberModifyInfo.getModifyPassword()));

        Member oldMember = oldMemberOptional.get();
        oldMember.modifyMember(memberModifyInfo);

        oldMember = memberRepository.save(oldMember);
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO(oldMember);

        return Optional.of(memberResponseDTO);
    }
}
