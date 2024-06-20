package io.elice.shoppingmall.member.controller;

import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberModifyInfo;
import io.elice.shoppingmall.member.entity.MemberRegister;
import io.elice.shoppingmall.member.entity.MemberResponseDTO;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.security.PrincipalDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.elice.shoppingmall.security.JwtTokenUtil;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenUtil util;


    //NOTE: ADMIN API
    @GetMapping("/admin/members")
    public ResponseEntity<List<Member>> getMembersForAdmin(){
        return new ResponseEntity<>(memberService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/admin/members/{id}")
    public ResponseEntity<Member> getMemberForAdmin(@PathVariable Long id){
        return new ResponseEntity<>(memberService.findByIdToMember(id), HttpStatus.OK);
    }

    @DeleteMapping("/admin/members/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return new ResponseEntity<>(memberService.delete(id), HttpStatus.OK);
    }

    //NOTE: USER API
    @GetMapping("/members/info")
    public ResponseEntity<MemberResponseDTO> getMember(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(memberService.findByUsernameToResponseDTO(userDetails.getUsername()), HttpStatus.OK);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponseDTO> postMember(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MemberModifyInfo memberModify){
        return new ResponseEntity<>(memberService.save(userDetails, memberModify), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDTO> register(@RequestBody MemberRegister memberRegister){
        return new ResponseEntity<>(memberService.save(memberRegister), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Member> login(@RequestBody MemberLogin memberLogin, HttpServletResponse response){
        memberService.login(memberLogin, response);

        return new ResponseEntity<>(memberService.login(memberLogin, response), HttpStatus.OK);
    }

    @PostMapping("/members-logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        return new ResponseEntity<>(memberService.logout(response), HttpStatus.OK);
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<String> unregister(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){
        return new ResponseEntity<>(memberService.delete(userDetails.getUsername(), response), HttpStatus.OK);
    }
}
