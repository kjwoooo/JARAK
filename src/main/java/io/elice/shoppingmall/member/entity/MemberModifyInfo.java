package io.elice.shoppingmall.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberModifyInfo {
    private String displayName;
    private String password;
    private String modifyPassword;
    private String phone;
    private Integer gender;
}
