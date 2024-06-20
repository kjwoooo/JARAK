package io.elice.shoppingmall.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "변경할 회원 정보")
public class MemberModifyInfo {
    @Schema(description = "변경할 회원 이름")
    private String displayName;
    @Schema(description = "기존 회원 비밀번호")
    private String password;
    @Schema(description = "변경할 회원 비밀번호")
    private String modifyPassword;
    @Schema(description = "수정할 회원 연락처")
    private String phone;
    @Schema(description = "수정할 회원 성별")
    private String gender;
}
