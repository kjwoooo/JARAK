package io.elice.shoppingmall.member.controller;

import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberModifyInfo;
import io.elice.shoppingmall.member.entity.MemberRegister;
import io.elice.shoppingmall.member.entity.MemberResponseDTO;
import io.elice.shoppingmall.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.elice.shoppingmall.security.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenUtil util;

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDTO>> getMembers(){
        return new ResponseEntity<>(memberService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponseDTO> getMember(@PathVariable Long id){
        return new ResponseEntity<>(memberService.findByIdToMemberResponseDTO(id), HttpStatus.OK);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponseDTO> postMember(@CookieValue("jwtToken") String jwtToken, @RequestBody MemberModifyInfo memberModify){
        return new ResponseEntity<>(memberService.save(jwtToken, memberModify), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberLogin memberLogin, HttpServletResponse response){
        Optional<Member> memberOptional = memberService.login(memberLogin);

        if(memberOptional.isEmpty())
            return new ResponseEntity<>("아이디 또는 비밀번호가 잘못되었습니다.", HttpStatus.NOT_FOUND);

        Member member = memberOptional.get();

        String jwtToken = util.createToken(member.getUsername(), member.getAothority(), util.getSECRET_KEY(), util.getEXPIRE_TIME_MS());

        Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), jwtToken);
        cookie.setMaxAge(util.getJWT_COOKIE_MAX_AGE());

        response.addCookie(cookie);
        response.addHeader(HttpHeaders.AUTHORIZATION, member.getAothority());

        return new ResponseEntity<>("로그인 완료.", HttpStatus.OK);
    }

    @GetMapping("/token-refresh")
    public ResponseEntity<String> tokenRefresh(@CookieValue String jwtToken, HttpServletResponse response){
        String username = util.getUsername(jwtToken);
        String authority = util.getAuthenticationInToken(jwtToken);
        String newJwtToken = util.createToken(username, authority, util.getSECRET_KEY(), util.getEXPIRE_TIME_MS());

        Cookie newCookie = new Cookie(util.getJWT_COOKIE_NAME(), newJwtToken);
        newCookie.setMaxAge(util.getJWT_COOKIE_MAX_AGE());

        response.addCookie(newCookie);
        response.addHeader(HttpHeaders.AUTHORIZATION, authority);

        return new ResponseEntity<>("토큰 재발급", HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        jwtTokenDestroy(response);

        return new ResponseEntity<>("로그아웃 완료.", HttpStatus.OK);
    }

    @DeleteMapping("/unregister/{id}")
    public void delete(HttpServletResponse response, @PathVariable Long id){
        jwtTokenDestroy(response);
        memberService.delete(id);
    }

    private void jwtTokenDestroy(HttpServletResponse response){
        Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody MemberRegister memberRegister){
        if(memberService.isExistUsername(memberRegister.getUsername()))
            return new ResponseEntity<>("이미 존재하는 ID 입니다.", HttpStatus.BAD_REQUEST);

        if(memberService.isExistEmail(memberRegister.getEmail()))
            return new ResponseEntity<>("이미 존재하는 Email 입니다.", HttpStatus.BAD_REQUEST);

        memberService.save(memberRegister);

        return new ResponseEntity<>("회원 등록 완료.", HttpStatus.CREATED);
    }
}
