package io.elice.shoppingmall.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 정보")
public class LoginInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "고유 번호")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "이메일")
    private String email;

    @Column
    @Schema(description = "비밀번호")
    private String password;

    @Column
    @Schema(description = "oAuth 정보 제공자")
    private String provider;

    @Column
    @Schema(description = "oAuth 고유 번호")
    private String providerId;

    public LoginInfo(MemberRegister memberRegister){
        this.email = memberRegister.getEmail();
        this.password = memberRegister.getPassword();
    }
}
