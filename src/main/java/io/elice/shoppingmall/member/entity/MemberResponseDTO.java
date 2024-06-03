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
public class MemberResponseDTO {
    private String displayName;
    private String email;
    private String username;
    private String phoneNumber;
    private Integer gender;
    private MemberAuthority admin;
    private UserMembership membership;

    public MemberResponseDTO(Member member){
        this.displayName = member.getDisplayName();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.gender = member.getGender();
        this.admin = MemberAuthority.valueOf(member.getAdmin());
        this.membership = UserMembership.valueOf(member.getMembership());
    }
}
