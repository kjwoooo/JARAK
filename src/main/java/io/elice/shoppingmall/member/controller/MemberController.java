package io.elice.shoppingmall.member.controller;

import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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

        if(members.isEmpty())
            return new ResponseEntity<Member>(HttpStatus.NOT_FOUND);

        return new ResponseEntity(members, HttpStatus.OK);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity getMember(@RequestParam Long id){
        Optional<Member> member = memberService.findById(id);

        if(member.isEmpty())
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity(new MemberResponseDTO(member.get()), HttpStatus.OK);
    }

    @PostMapping("/members-login")
    public ResponseEntity login(@RequestBody MemberLogin memberLogin, HttpServletResponse response){
        Optional<Member> memberOptional = memberService.login(memberLogin);

        if(memberOptional.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        Member member = memberOptional.get();

        String jwtToken = JwtTokenUtil.createToken(member.getUsername(), member.getAdmin(), util.getSECRET_KEY(), util.getEXPIRE_TIME_MS());

        Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), jwtToken);
        cookie.setMaxAge(util.getJWT_COOKIE_MAX_AGE());

        response.addCookie(cookie);
        response.addHeader(HttpHeaders.AUTHORIZATION, member.getAdmin());

        return new ResponseEntity(member, HttpStatus.OK);
    }

    @GetMapping("/members-logout")
    public ResponseEntity logout(HttpServletResponse response){
        jwtTokenDestroy(response);

        return new ResponseEntity("logout", HttpStatus.OK);
    }

    @DeleteMapping("/members-unregister")
    public void delete(HttpServletResponse response, @RequestParam Long id){
        jwtTokenDestroy(response);
        memberService.delete(id);
    }

    private void jwtTokenDestroy(HttpServletResponse response){
        Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @PostMapping("/members-register")
    public ResponseEntity register(@RequestBody MemberRegister memberDTO){
        Optional<Member> newMember = memberService.save(memberDTO);
        if(newMember.isEmpty())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity(newMember.get(), HttpStatus.CREATED);
    }

    @GetMapping("/admin")
    public ResponseEntity admin(){
        return new ResponseEntity("admin", HttpStatus.OK);
    }
}
