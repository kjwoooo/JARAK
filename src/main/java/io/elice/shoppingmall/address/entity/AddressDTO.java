package io.elice.shoppingmall.address.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long memberId;
    private String recipientName;
    private String zipcode;
    private String addr;
    private String addrDetail;
    private String recipientTel;
    private String deliveryReq;
    private String defDestination;

    public Address toEntity(){
        Address address = new Address();

        address.setRecipientName(recipientName);
        address.setZipcode(zipcode);
        address.setAddr(addr);
        address.setAddrDetail(addrDetail);
        address.setRecipientTel(recipientTel);
        address.setDeliveryReq(deliveryReq);
        address.setDefDestination(defDestination);

        return address;
    }
}
