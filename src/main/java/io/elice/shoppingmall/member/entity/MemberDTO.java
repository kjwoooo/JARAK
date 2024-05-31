package io.elice.shoppingmall.member.entity;

import io.elice.shoppingmall.member.MemberAuthority;
import io.elice.shoppingmall.member.UserMembership;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String displayName;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private Integer gender;
    private MemberAuthority admin;
    private UserMembership membership;

    public MemberDTO(Member member){
        this.displayName = member.getDisplayName();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.phoneNumber = member.getPhoneNumber();
        this.gender = member.getGender();
        this.admin = MemberAuthority.valueOf(member.getAdmin());
        this.membership = UserMembership.valueOf(member.getMembership());
    }

    public Member toEntity(){
        Member member = new Member();
        member.setDisplayName(displayName);
        member.setEmail(email);
        member.setUsername(username);
        member.setPassword(password);
        member.setPhoneNumber(phoneNumber);
        member.setGender(gender);
        member.setAdmin(admin.name());
        member.setMembership(membership.name());

        return member;
    }

    public Member toEntity(Long id){
        Member user = toEntity();
        user.setId(id);

        return user;
    }
}
