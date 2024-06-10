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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.regex.Pattern;
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

    /**
     * 이메일 형식 검증
     * @param email
     */
    private void validEmail(String email){
        String EMAIL_REGEX = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        if(!Pattern.matches(EMAIL_REGEX, email))
            throw new CustomException(ErrorCode.NOT_MATCH_EMAIL);
    }

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

    public String login(MemberLogin loginInfo, HttpServletResponse response){
        Member member = memberRepository.findByUsername(loginInfo.getUsername()).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        memberMatchPassword(member, loginInfo.getPassword());
        createJwtTokenCookie(member, response);

        return "로그인 성공";
    }

    public String tokenRefresh(String jwtToken, HttpServletResponse response){
        String username = util.getUsername(jwtToken);
        Member member = memberRepository.findByUsername(username).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        createJwtTokenCookie(member, response);
        return "토큰 재발급 성공";
    }

    private void createJwtTokenCookie(Member member, HttpServletResponse response){
        String jwtToken = util.createToken(member.getUsername(), member.getAothority());

        Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), jwtToken);
        cookie.setMaxAge(util.getJWT_COOKIE_MAX_AGE());

        response.addCookie(cookie);
    }

    public String logout(HttpServletResponse response){
        jwtTokenDestroy(response);
        return "로그아웃";
    }

    private void jwtTokenDestroy(HttpServletResponse response){
        Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public String delete(String jwtToken, HttpServletResponse response){
        String username = util.getUsername(jwtToken);
        Member member = memberRepository.findByUsername(username).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        memberRepository.delete(member);
        jwtTokenDestroy(response);

        return "회원 정보 삭제";
    }

    private void existUsername(String username){
        if(memberRepository.existsByUsername(username))
            throw new CustomException(ErrorCode.EXIST_USERNAME);
    }

    /**
     * 이메일 중복검사
     * @param email
     */
    private void existEmail(String email){
        if(loginInfoRepository.existsByEmail(email))
            throw new CustomException(ErrorCode.EXIST_EMAIL);
    }

    private void memberMatchPassword(Member member, String password){
        if(!encoder.matches(password, member.getLoginInfo().getPassword()))
            throw new CustomException(ErrorCode.MEMBER_PASSWROD_WRONG);
    }

    /**
     * 회원가입
     * @param memberRegister
     * @return 해당 회원정보
     */
    public MemberResponseDTO save(MemberRegister memberRegister){
        existUsername(memberRegister.getUsername());
        validEmail(memberRegister.getEmail());
        existEmail(memberRegister.getEmail());

        memberRegister.setPassword(encoder.encode(memberRegister.getPassword()));

        LoginInfo loginInfo = new LoginInfo(memberRegister);
        loginInfo = loginInfoRepository.save(loginInfo);

        Member member = memberRegister.toUserEntity();
        member.setLoginInfo(loginInfo);

        member = memberRepository.save(member);

        return new MemberResponseDTO(member);
    }

    /**
     * 회원정보 수정
     * @param jwtToken
     * @param memberModifyInfo
     * @return 변경된 회원정보
     */
    public MemberResponseDTO save(String jwtToken, MemberModifyInfo memberModifyInfo){
        String username = util.getUsername(jwtToken);
        Member oldMember = findByUsername(username);

        memberMatchPassword(oldMember, memberModifyInfo.getPassword());
        memberModifyInfo.setModifyPassword(encoder.encode(memberModifyInfo.getModifyPassword()));
        oldMember.modifyMember(memberModifyInfo);

        return new MemberResponseDTO(memberRepository.save(oldMember));
    }
}
