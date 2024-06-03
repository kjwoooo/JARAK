package io.elice.shoppingmall.address.controller;


import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.service.AddressService;
import io.elice.shoppingmall.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;



    @GetMapping("/addresses/{memberId}")
    public ResponseEntity getMemberAddresses(@RequestParam Long memberId){
        List<Address> addressList = addressService.findByMemberId(memberId);
        if(addressList.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(addressList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{memberId}/{addressId}")
    public ResponseEntity getMemberAddress(@RequestParam Long memberId, @RequestParam Long addressId){
        Address address = addressService.findByAddressIdAndMemberId(memberId, addressId).orElse(null);

        if(address==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(address, HttpStatus.OK);
    }
}
