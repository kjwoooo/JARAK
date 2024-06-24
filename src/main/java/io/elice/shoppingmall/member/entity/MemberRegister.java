package io.elice.shoppingmall.member.entity;

import io.elice.shoppingmall.member.MemberAuthority;
import io.elice.shoppingmall.member.Membership;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 가입 정보")
public class MemberRegister {
    @Schema(description = "이름")
    private String displayName;
    @Schema(description = "이메일")
    private String email;
    @Schema(description = "아이디")
    private String username;
    @Schema(description = "비밀번호")
    private String password;
    @Schema(description = "연락처")
    private String phone;
    @Schema(description = "성별")
    private String gender;

    public MemberRegister(Member member){
        this.displayName = member.getDisplayName();
        this.username = member.getUsername();
        this.email = member.getLoginInfo().getEmail();
        this.password = member.getLoginInfo().getPassword();
        this.phone = member.getPhone();
        this.gender = member.getGender();
    }

    private Member toEntity(){
        Member member = new Member();

        member.setDisplayName(displayName);
        member.setUsername(username);
        member.setPhone(phone);
        member.setGender(gender);
        member.setMembership(Membership.BRONZE.name());

        return member;
    }

    public Member toUserEntity(){
        Member member = toEntity();
        member.setAuthority(MemberAuthority.USER.name());

        return member;
    }

    public Member toAdminEntity(){
        Member member = toEntity();
        member.setAuthority(MemberAuthority.ADMIN.name());

        return member;
    }
}
