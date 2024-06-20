package io.elice.shoppingmall.member.controller;

import io.elice.shoppingmall.address.entity.AddressResponseDTO;
import io.elice.shoppingmall.member.entity.MemberLogin;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberModifyInfo;
import io.elice.shoppingmall.member.entity.MemberRegister;
import io.elice.shoppingmall.member.entity.MemberResponseDTO;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.ErrorResponse;
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
@Tag(name = "회원 관리", description = "로그인된 회원 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenUtil util;


    //NOTE: ADMIN API
    @Operation(summary = "존재하는 모든 회원 조회 (관리자용)", description = "현재 존재하는 모든 회원을 조회합니다. 관리자만 접근할 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Member.class))))
    })
    @GetMapping("/admin/members")
    public ResponseEntity<List<Member>> getMembersForAdmin(){
        return new ResponseEntity<>(memberService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "특정 회원 조회 (관리자용)", description = "특정 회원을 조회합니다. 관리자만 접근할 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Member.class))),
        @ApiResponse(responseCode = "400", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/admin/members/{id}")
    public ResponseEntity<Member> getMemberForAdmin(
        @Parameter(name = "id", description = "찾으려는 회원의 고유 번호", in = ParameterIn.PATH) @PathVariable Long id){
        return new ResponseEntity<>(memberService.findByIdToMember(id), HttpStatus.OK);
    }

    @Operation(summary = "특정 회원 삭제 (관리자용)", description = "특정 회원을 삭제합니다. 관리자만 접근할 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/admin/members/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return new ResponseEntity<>(memberService.delete(id), HttpStatus.OK);
    }

    //NOTE: USER API
    @Operation(summary = "로그인한 회원의 정보 조회", description = "로그인한 회원의 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = MemberResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/members/info")
    public ResponseEntity<MemberResponseDTO> getMember(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(memberService.findByUsernameToResponseDTO(userDetails.getUsername()), HttpStatus.OK);
    }
    
    @Operation(summary = "로그인한 회원의 정보 수정", description = "로그인한 회원의 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = MemberResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "비밀번호가 틀렸습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/members")
    public ResponseEntity<MemberResponseDTO> postMember(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "memberModify", description = "변경할 회원 정보", in = ParameterIn.PATH) @RequestBody MemberModifyInfo memberModify){
        return new ResponseEntity<>(memberService.save(userDetails, memberModify), HttpStatus.OK);
    }

    @Operation(summary = "서비스 이용을 위한 회원가입", description = "서비스 이용을 위해서 회원 가입을 요청합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = MemberResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "이미 존재하는 아이디 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "이미 존재하는 이메일 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<MemberResponseDTO> register(
        @Parameter(name = "memberRegister", description = "회원 가입 정보", in = ParameterIn.PATH)@RequestBody MemberRegister memberRegister){
        return new ResponseEntity<>(memberService.save(memberRegister), HttpStatus.CREATED);
    }

    @Operation(summary = "서비스 이용을 위한 로그인", description = "서비스 이용을 위해서 로그인 요청을 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = MemberResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "비밀번호가 틀렸습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "존재하지 않는 아이디 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<Member> login(@RequestBody MemberLogin memberLogin, HttpServletResponse response){
        return new ResponseEntity<>(memberService.login(memberLogin, response), HttpStatus.OK);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 요청을 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/members-logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        return new ResponseEntity<>(memberService.logout(response), HttpStatus.OK);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 요청을 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원탈퇴 성공", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "존재하지 않는 회원 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/unregister")
    public ResponseEntity<String> unregister(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){
        return new ResponseEntity<>(memberService.delete(userDetails.getUsername(), response), HttpStatus.OK);
    }
}
