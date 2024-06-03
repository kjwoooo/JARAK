package io.elice.shoppingmall.member.controller;

import io.elice.shoppingmall.member.entity.LoginInfo;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberDTO;
import io.elice.shoppingmall.member.entity.MemberResponseDTO;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.security.SecurityConfig;
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

    @GetMapping("/members")
    public ResponseEntity getMembers(){
        List<MemberResponseDTO> members = memberService.findAll().stream().map(
            MemberResponseDTO::new).toList();

        if(members.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(members, HttpStatus.OK);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity getMember(@RequestParam Long id){
        Member member = memberService.findById(id).orElse(null);

        if(member == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(new MemberResponseDTO(member), HttpStatus.OK);
    }

    @PostMapping("/loginTest")
    public ResponseEntity login(@RequestBody LoginInfo loginInfo, HttpServletResponse response){
        Member member = memberService.login(loginInfo);

        if(member == null){
            System.out.println("member null");
            return new ResponseEntity(loginInfo, HttpStatus.NOT_FOUND);
        }

        String secretKey = SecurityConfig.getSecretKey();
        long expireTimeMs = SecurityConfig.getExpireTimeMs();

        String jwtToken = JwtTokenUtil.createToken(member.getUsername(), member.getAdmin(), secretKey, expireTimeMs);

        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setMaxAge(60 * 60 * 24);

        response.addCookie(cookie);
        response.addHeader(HttpHeaders.AUTHORIZATION, member.getAdmin());

        return new ResponseEntity(member, HttpStatus.OK);
    }

    @GetMapping("/member/info")
    public ResponseEntity memberInfo(){
        return new ResponseEntity("member info", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody MemberDTO memberDTO){
        Member newMember = memberService.save(memberDTO);

        if(newMember == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(newMember, HttpStatus.CREATED);
    }

    @GetMapping("/admin")
    public ResponseEntity admin(){
        return new ResponseEntity("admin", HttpStatus.OK);
    }
}
