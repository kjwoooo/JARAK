package io.elice.shoppingmall.address.controller;


import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.entity.AddressDTO;
import io.elice.shoppingmall.address.entity.AddressResponseDTO;
import io.elice.shoppingmall.address.service.AddressService;
import io.elice.shoppingmall.member.entity.Member;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/addresses/{memberId}")
    public ResponseEntity<?> getMemberAddresses(@PathVariable Long memberId){
        List<AddressResponseDTO> addressList = addressService.findByMemberId(memberId).stream().map(AddressResponseDTO::new).toList();

        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{memberId}/{addressId}")
    public ResponseEntity<?> getMemberAddress(@PathVariable Long memberId, @PathVariable Long addressId){
        Optional<Address> addressOptional = addressService.findByAddressIdAndMemberId(memberId, addressId);

        if(addressOptional.isEmpty()){
            return new ResponseEntity<>("잘못된 회원 정보 입니다.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(addressOptional.get(), HttpStatus.OK);
    }

    @PostMapping("/addresses/{memberId}")
    public ResponseEntity<?> postMemberAddress(@PathVariable Long memberId, @RequestBody AddressDTO addressDto){
        Optional<AddressResponseDTO> addressResponseDTOOptional = addressService.save(memberId, addressDto);

        if(addressResponseDTOOptional.isEmpty())
            return new ResponseEntity<>("잘못된 회원 정보 입니다.", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(addressResponseDTOOptional.get(), HttpStatus.CREATED);
    }
}
