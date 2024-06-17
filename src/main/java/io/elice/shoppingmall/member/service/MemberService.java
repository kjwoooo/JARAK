package io.elice.shoppingmall.member.service;


import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.LoginInfo;
import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberModifyInfo;
import io.elice.shoppingmall.member.entity.MemberRegister;
import io.elice.shoppingmall.member.entity.MemberResponseDTO;
import io.elice.shoppingmall.member.repository.MemberRepository;
import io.elice.shoppingmall.security.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final LoginInfoService loginInfoService;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil util;
    private final String EMAIL_REGEX = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    /**
     * 이메일 형식 검증
     * @param email
     */
    private void validEmail(String email){
        if(!Pattern.matches(EMAIL_REGEX, email))
            throw new CustomException(ErrorCode.NOT_MATCH_EMAIL);
    }

    /**
     * 모든 회원 검색
     * @return MemberResponseDTO List
     */
    public List<MemberResponseDTO> findAllReturnMemberResponseDTOList(){
        return findAll().stream().map(MemberResponseDTO::new).toList();
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }


    /**
     * @deprecated findByUsername 사용 권장
     * @param jwtToken
     * @return
     */
    @Deprecated
    public Member findByJwtToken(String jwtToken) {
        String username = util.getUsername(jwtToken);
        return findByUsername(username);
    }

    /**
     * @deprecated findByUsernameToResponseDTO 사용 권장
     * @param jwtToken
     * @return MemberResponseDTO
     */
    @Deprecated
    public MemberResponseDTO findByJwtTokenToResponseDTO(String jwtToken){
        return new MemberResponseDTO(findByJwtToken(jwtToken));
    }

    /**
     * id로 회원 검색
     * @param id
     * @return Member
     */
    public Member findByIdToMember(Long id){
        return memberRepository.findById(id).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public MemberResponseDTO findByIdToResponseDTO(Long id){
        Member member = memberRepository.findById(id).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        return new MemberResponseDTO(member);
    }

    /**
     * username(ID)로 회원 검색
     * @param username
     * @return
     */
    public Member findByUsername(String username){
        return memberRepository.findByUsername(username).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public MemberResponseDTO findByUsernameToResponseDTO(String username){
        return new MemberResponseDTO(findByUsername(username));
    }

    /**
     * 회원이 입력한 ID, Password를 바탕으로
     * 해당 회원이 존재하는지 검증
     * @param memberLogin 로그인할 때, 회원이 입력한 ID, Password 정보
     * @param response
     * @returnwb
     */
    public Member login(MemberLogin memberLogin, HttpServletResponse response){
        Member member = findByUsername(memberLogin.getUsername());

        loginInfoService.matchPassword(member.getLoginInfo(), memberLogin.getPassword());
        createJwtTokenCookie(member, response);
        return member;
    }

    public Member OAuthLogin(OAuth2User user, HttpServletResponse response){
        String providerId = user.getName();

        LoginInfo loginInfo = loginInfoService.findByProviderId(providerId);
        Member member = findByLoginInfo(loginInfo);

        createJwtTokenCookie(member, response);
        return member;
    }

    public void createJwtTokenCookie(Member member, HttpServletResponse response){
        String jwtToken = util.createToken(member.getUsername(), member.getAuthority());

        Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), jwtToken);
        cookie.setMaxAge(util.getJWT_COOKIE_MAX_AGE());

        response.addCookie(cookie);
    }

    public String logout(HttpServletResponse response){
        util.tokenDestroy(response);
        return "로그아웃";
    }

    public String delete(String username, HttpServletResponse response){
        Member member = findByUsername(username);

        memberRepository.delete(member);
        util.tokenDestroy(response);

        return "회원 탈퇴";
    }

    /**
     * 관리자가 특정 회원 삭제
     * @param id
     * @return
     */
    public String delete(Long id){
        Member member = findByIdToMember(id);
        memberRepository.delete(member);
        return "회원 정보 삭제";
    }

    private void existUsername(String username){
        if(memberRepository.existsByUsername(username))
            throw new CustomException(ErrorCode.EXIST_USERNAME);
    }

    public Member findByLoginInfo(LoginInfo loginInfo){
        return memberRepository.findByLoginInfo(loginInfo).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public Member save(Member member){
        return memberRepository.save(member);
    }

    /**
     * 회원가입
     * @param memberRegister
     * @return 해당 회원정보
     */
    public MemberResponseDTO save(MemberRegister memberRegister){
        existUsername(memberRegister.getUsername());
        validEmail(memberRegister.getEmail());
        loginInfoService.existsEmail(memberRegister.getEmail());

        memberRegister.setPassword(encoder.encode(memberRegister.getPassword()));

        LoginInfo loginInfo = new LoginInfo(memberRegister);
        loginInfo = loginInfoService.save(loginInfo);

        Member member = memberRegister.toUserEntity();
        member.setLoginInfo(loginInfo);

        member = memberRepository.save(member);

        return new MemberResponseDTO(member);
    }

    public MemberResponseDTO save(UserDetails userDetails, MemberModifyInfo memberModifyInfo){
        Member oldMember = findByUsername(userDetails.getUsername());

        loginInfoService.matchPassword(oldMember.getLoginInfo(), memberModifyInfo.getPassword());

        memberModifyInfo.setModifyPassword(encoder.encode(memberModifyInfo.getModifyPassword()));
        memberModifyInfo.setPassword(encoder.encode(memberModifyInfo.getPassword()));
        oldMember.modifyMember(memberModifyInfo);

        LoginInfo loginInfo = oldMember.getLoginInfo();
        loginInfo.setPassword(memberModifyInfo.getModifyPassword());

        loginInfo = loginInfoService.save(loginInfo);
        oldMember.setLoginInfo(loginInfo);

        return new MemberResponseDTO(memberRepository.save(oldMember));
    }
}
