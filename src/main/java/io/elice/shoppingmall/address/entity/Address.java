package io.elice.shoppingmall.address.entity;

import io.elice.shoppingmall.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String addr;

    @Column(nullable = false)
    private String addrDetail;

    @Column(nullable = false)
    private String recipientTel;

    @Column(nullable = false)
    private String addrName;

    @Column(nullable = false)
    private String deliveryReq;

    public void updateAddress(AddressDTO addressDTO){
        recipientName = addressDTO.getRecipientName();
        zipcode = addressDTO.getZipcode();
        addr = addressDTO.getAddr();
        addrDetail = addressDTO.getAddrDetail();
        recipientTel = addressDTO.getRecipientTel();
        addrName = addressDTO.getAddrName();
        deliveryReq = addressDTO.getDeliveryReq();
    }
}
