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
    public ResponseEntity<MemberResponseDTO> postMember(@CookieValue String jwtToken, @RequestBody MemberModifyInfo memberModify){
        return new ResponseEntity<>(memberService.save(jwtToken, memberModify), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberLogin memberLogin, HttpServletResponse response){
        memberService.login(memberLogin, response);

        return new ResponseEntity<>(memberService.login(memberLogin, response), HttpStatus.OK);
    }

    @GetMapping("/token-refresh")
    public ResponseEntity<String> tokenRefresh(@CookieValue String jwtToken, HttpServletResponse response){
        return new ResponseEntity<>(memberService.tokenRefresh(jwtToken, response), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        return new ResponseEntity<>(memberService.logout(response), HttpStatus.OK);
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<String> delete(@CookieValue String jwtToken, HttpServletResponse response){
        return new ResponseEntity<>(memberService.delete(jwtToken, response), HttpStatus.OK);
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
