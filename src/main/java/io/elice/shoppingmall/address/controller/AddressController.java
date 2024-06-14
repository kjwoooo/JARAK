package io.elice.shoppingmall.address.controller;


import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.entity.AddressDTO;
import io.elice.shoppingmall.address.entity.AddressResponseDTO;
import io.elice.shoppingmall.address.service.AddressService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressResponseDTO>> getMemberAddresses(@CookieValue String jwtToken){
        return new ResponseEntity<>(addressService.findAllByJwtTokenAndReturnResponseDTO(jwtToken), HttpStatus.OK);
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<AddressResponseDTO> getMemberAddress(@CookieValue String jwtToken, @PathVariable Long id){
        return new ResponseEntity<>(addressService.findByJwtTokenAndAddressId(jwtToken, id), HttpStatus.OK);
    }

    @PostMapping("/addresses/{id}")
    public ResponseEntity<AddressResponseDTO> postMemberAddress(@CookieValue String jwtToken, @PathVariable Long id, @RequestBody AddressDTO addressDto){
        return new ResponseEntity<>(addressService.saveAndReturnResponseDTO(jwtToken, id, addressDto), HttpStatus.CREATED);
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressResponseDTO> updateMemberAddress(@CookieValue String jwtToken, @RequestBody AddressDTO addressDto){
        return new ResponseEntity<>(addressService.saveAndReturnResponseDTO(jwtToken, addressDto), HttpStatus.CREATED);
    }

//    @DeleteMapping("/addresses/{id}")
//    public ResponseEntity<String> deleteMemberAddress(@CookieValue String jwtToken, @PathVariable Long id){
//
//    }
}
