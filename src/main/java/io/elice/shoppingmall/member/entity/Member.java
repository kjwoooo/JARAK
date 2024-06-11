package io.elice.shoppingmall.member.entity;

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
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, unique = true)
    private String username;

    @OneToOne
    @JoinColumn(name="loginInfo_id", nullable = false)
    private LoginInfo loginInfo;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String aothority;

    @Column(nullable = false)
    private String membership;

    public void modifyMember(MemberModifyInfo memberModify){
        this.displayName = memberModify.getDisplayName();
        this.loginInfo.setPassword(memberModify.getModifyPassword());
        this.phone = memberModify.getPhone();
        this.gender = memberModify.getGender();
    }
}
