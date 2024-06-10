package io.elice.shoppingmall.member.service;


import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.LoginInfo;
import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberModifyInfo;
import io.elice.shoppingmall.member.entity.MemberRegister;
import io.elice.shoppingmall.member.entity.MemberResponseDTO;
import io.elice.shoppingmall.member.repository.LoginInfoRepository;
import io.elice.shoppingmall.member.repository.MemberRepository;
import io.elice.shoppingmall.security.JwtTokenUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil util;


    public List<MemberResponseDTO> findAll(){
        return memberRepository.findAll().stream().map(MemberResponseDTO::new).toList();
    }

    public Member findByIdToMember(Long id){
        return memberRepository.findById(id).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public MemberResponseDTO findByIdToMemberResponseDTO(Long id){
        Member member = findByIdToMember(id);

        return new MemberResponseDTO(member);
    }

    public Member findByUsername(String username){
        return memberRepository.findByUsername(username).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public Optional<Member> login(MemberLogin loginInfo){
        Optional<Member> memberOptional = memberRepository.findByUsername(loginInfo.getUsername());

        if(memberOptional.isEmpty())
            return Optional.empty();

        if(!encoder.matches(loginInfo.getPassword(), memberOptional.get().getLoginInfo().getPassword()))
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

    public boolean isExistEmail(String email){
        return loginInfoRepository.existsByEmail(email);
    }

    private void memberMatchPassword(Member member, String password){
        if(!encoder.matches(password, member.getLoginInfo().getPassword()))
            throw new CustomException(ErrorCode.MEMBER_PASSWROD_WRONG);
    }

    public Optional<MemberResponseDTO> save(MemberRegister memberRegister){
        memberRegister.setPassword(encoder.encode(memberRegister.getPassword()));

        LoginInfo loginInfo = new LoginInfo(memberRegister);
        loginInfo = loginInfoRepository.save(loginInfo);

        Member member = memberRegister.toUserEntity();
        member.setLoginInfo(loginInfo);

        member = memberRepository.save(member);

        return Optional.of(new MemberResponseDTO(member));
    }

//    public MemberResponseDTO save(Long id, MemberModifyInfo memberModifyInfo){
//        Member oldMember = findByIdToMember(id);
//        memberMatchPassword(oldMember, memberModifyInfo.getPassword());
//
//        memberModifyInfo.setModifyPassword(encoder.encode(memberModifyInfo.getModifyPassword()));
//
//        oldMember.modifyMember(memberModifyInfo);
//
//        return new MemberResponseDTO(memberRepository.save(oldMember));
//    }

    public MemberResponseDTO save(String jwtToken, MemberModifyInfo memberModifyInfo){
        String username = util.getUsername(jwtToken);
        Member oldMember = findByUsername(username);

        memberMatchPassword(oldMember, memberModifyInfo.getPassword());
        memberModifyInfo.setModifyPassword(encoder.encode(memberModifyInfo.getModifyPassword()));
        oldMember.modifyMember(memberModifyInfo);

        return new MemberResponseDTO(memberRepository.save(oldMember));
    }
}
