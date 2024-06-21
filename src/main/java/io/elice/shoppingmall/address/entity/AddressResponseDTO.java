package io.elice.shoppingmall.address.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "주소 정보")
public class AddressResponseDTO {
    @Schema(description = "주소 고유 번호")
    private Long id;
    @Schema(description = "회원 고유 번호")
    private Long memberId;
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

    public AddressResponseDTO(Address address){
        this.id = address.getId();
        this.memberId = address.getMember().getId();
        this.recipientName = address.getRecipientName();
        this.zipcode = address.getZipcode();
        this.addr = address.getAddr();
        this.addrDetail = address.getAddrDetail();
        this.recipientTel = address.getRecipientTel();
        this.addrName = address.getAddrName();
        this.deliveryReq = address.getDeliveryReq();
    }
}
