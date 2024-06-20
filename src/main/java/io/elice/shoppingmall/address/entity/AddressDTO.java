package io.elice.shoppingmall.address.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주소 정보")
public class AddressDTO {
    @Schema(description = "수령자 이름")
    private String recipientName;
    @Schema(description = "우편번호")
    private String zipcode;
    @Schema(description = "주소")
    private String addr;
    @Schema(description = "상세 주소")
    private String addrDetail;
    @Schema(description = "수령자 연락처")
    private String recipientTel;
    @Schema(description = "주소지 명")
    private String addrName;
    @Schema(description = "배송 시 요청사항")
    private String deliveryReq;

    public Address toEntity(){
        Address address = new Address();

        address.setRecipientName(recipientName);
        address.setZipcode(zipcode);
        address.setAddr(addr);
        address.setAddrDetail(addrDetail);
        address.setRecipientTel(recipientTel);
        address.setAddrName(addrName);
        address.setDeliveryReq(deliveryReq);

        return address;
    }
}
