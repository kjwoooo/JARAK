package io.elice.shoppingmall.address.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressResponseDTO {
    private Long id;
    private Long memberId;
    private String recipientName;
    private String zipcode;
    private String addr;
    private String addrDetail;
    private String recipientTel;
    private String addrName;
    private String deliveryReq;

    public AddressResponseDTO(Address address){
        this.id = address.getId();
        this.memberId = address.getId();
        this.recipientName = address.getRecipientName();
        this.zipcode = address.getZipcode();
        this.addr = address.getAddr();
        this.addrDetail = address.getAddrDetail();
        this.recipientTel = address.getAddrDetail();
        this.addrName = address.getAddrName();
        this.deliveryReq = address.getDeliveryReq();
    }
}
