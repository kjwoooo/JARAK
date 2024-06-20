package io.elice.shoppingmall.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 정보")
public class MemberResponseDTO {
    @Schema(description = "회원 이름")
    private String displayName;
    @Schema(description = "회원 이메일")
    private String email;
    @Schema(description = "회원 아이디")
    private String username;
    @Schema(description = "회원 연락처")
    private String phone;
    @Schema(description = "회원 성별")
    private String gender;
    @Schema(description = "회원 멤버십")
    private String membership;

    public MemberResponseDTO(Member member){
        this.displayName = member.getDisplayName();
        this.username = member.getUsername();
        this.email = member.getLoginInfo().getEmail();
        this.phone = member.getPhone();
        this.gender = member.getGender();
        this.membership = member.getMembership();
    }
}
