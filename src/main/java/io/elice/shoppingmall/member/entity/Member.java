package io.elice.shoppingmall.member.entity;

import io.elice.shoppingmall.entity.baseEntity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 엔티티")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "회원 고유 번호")
    private Long id;


    @Column(nullable = false)
    @Schema(description = "회원 이름")
    private String displayName;


    @Column
    @Schema(description = "회원 아이디")
    private String username;

    @OneToOne
    @JoinColumn(name="loginInfo_id", nullable = false)
    @Schema(description = "회원 로그인 정보")
    private LoginInfo loginInfo;

    @Column
    @Schema(description = "회원 연락처")
    private String phone;

    @Column
    @Schema(description = "회원 성별")
    private String gender;

    @Column(nullable = false)
    @Schema(description = "회원 권한")
    private String authority;

    @Column(nullable = false)
    @Schema(description = "회원 멤버십")
    private String membership;

    public void modifyMember(MemberModifyInfo memberModify){
        this.displayName = memberModify.getDisplayName();
        this.phone = memberModify.getPhone();
        this.gender = memberModify.getGender();
    }
}
