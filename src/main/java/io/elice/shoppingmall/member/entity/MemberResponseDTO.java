package io.elice.shoppingmall.member.entity;

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
    private String phone;
    private String gender;
    private String membership;

    public MemberResponseDTO(Member member){
        this.displayName = member.getDisplayName();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.gender = member.getGender();
        this.membership = member.getMembership();
    }
}
