package io.elice.shoppingmall.member.entity;

import io.elice.shoppingmall.member.MemberAuthority;
import io.elice.shoppingmall.member.Membership;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegister {
    private String displayName;
    private String email;
    private String username;
    private String password;
    private String phone;
    private Integer gender;

    public MemberRegister(Member member){
        this.displayName = member.getDisplayName();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.phone = member.getPhone();
        this.gender = member.getGender();
    }

    private Member toEntity(){
        Member member = new Member();

        member.setDisplayName(displayName);
        member.setEmail(email);
        member.setUsername(username);
        member.setPassword(password);
        member.setPhone(phone);
        member.setGender(gender);
        member.setMembership(Membership.BRONZE.name());

        return member;
    }

    public Member toUserEntity(){
        Member member = toEntity();
        member.setAdmin(MemberAuthority.USER.name());

        return member;
    }

    public Member toAdminEntity(){
        Member member = toEntity();
        member.setAdmin(MemberAuthority.ADMIN.name());

        return member;
    }
}
