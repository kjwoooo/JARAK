package io.elice.shoppingmall.member.controller;

import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberDTO;
import io.elice.shoppingmall.member.entity.MemberResponseDTO;
import io.elice.shoppingmall.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.elice.shoppingmall.security.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenUtil util;

    @GetMapping("/members")
    public ResponseEntity getMembers(){
        List<MemberResponseDTO> members = memberService.findAll().stream().map(
            MemberResponseDTO::new).toList();

        if(members.isEmpty()){
            return new ResponseEntity<Member>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(members, HttpStatus.OK);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity getMember(@RequestParam Long id){
        Member member = memberService.findById(id).orElse(null);

        if(member == null){
            return new ResponseEntity<MemberResponseDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<MemberResponseDTO>(new MemberResponseDTO(member), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLogin memberLogin, HttpServletResponse response){
        Member member = memberService.login(memberLogin);

        if(member == null){
            return new ResponseEntity(memberLogin, HttpStatus.NOT_FOUND);
        }

        String jwtToken = JwtTokenUtil.createToken(member.getUsername(), member.getAdmin(), util.getSECRET_KEY(), util.getEXPIRE_TIME_MS());

        Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), jwtToken);
        cookie.setMaxAge(util.getJWT_COOKIE_MAX_AGE());

        response.addCookie(cookie);
        response.addHeader(HttpHeaders.AUTHORIZATION, member.getAdmin());

        return new ResponseEntity(member, HttpStatus.OK);
    }

    @GetMapping("/member-logout")
    public ResponseEntity logout(HttpServletResponse response){
        Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return new ResponseEntity("logout", HttpStatus.OK);
    }

    @GetMapping("/member/info")
    public ResponseEntity memberInfo(){
        return new ResponseEntity("member info", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody MemberDTO memberDTO){
        Member newMember = memberService.save(memberDTO);
        return new ResponseEntity(newMember, HttpStatus.CREATED);
    }

    @GetMapping("/admin")
    public ResponseEntity admin(){
        return new ResponseEntity("admin", HttpStatus.OK);
    }
}
