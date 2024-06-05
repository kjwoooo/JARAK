package io.elice.shoppingmall.member.entity;

import io.elice.shoppingmall.member.MemberAuthority;
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
    private MemberAuthority admin;

    public MemberRegister(Member member){
        this.displayName = member.getDisplayName();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.phone = member.getPhone();
        this.gender = member.getGender();
        this.admin = MemberAuthority.valueOf(member.getAdmin());
    }

    public Member toUserEntity(){
        Member member = new Member();
        member.setDisplayName(displayName);
        member.setEmail(email);
        member.setUsername(username);
        member.setPassword(password);
        member.setPhone(phone);
        member.setGender(gender);
        member.setAdmin(admin.name());
        member.setMembership(MemberAuthority.USER.name());

        return member;
    }

    public Member toUserEntity(Long id){
        Member member = toUserEntity();
        member.setId(id);

        return member;
    }

    public Member toAdminEntity(){
        Member member = new Member();
        member.setDisplayName(displayName);
        member.setEmail(email);
        member.setUsername(username);
        member.setPassword(password);
        member.setPhone(phone);
        member.setGender(gender);
        member.setAdmin(admin.name());
        member.setMembership(MemberAuthority.ADMIN.name());

        return member;
    }

    public Member toAdminEntity(Long id){
        Member member = toAdminEntity();
        member.setId(id);

        return member;
    }
}
