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
        return new ResponseEntity<>(addressService.findAllByJwtToken(jwtToken), HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressResponseDTO> getMemberAddress(@CookieValue String jwtToken, @PathVariable Long addressId){
        return new ResponseEntity<>(addressService.findByJwtTokenAndAddressId(jwtToken, addressId), HttpStatus.OK);
    }

    @PostMapping("/addresses/{id}")
    public ResponseEntity<?> postMemberAddress(@CookieValue String jwtToken, @PathVariable Long id, @RequestBody AddressDTO addressDto){
        return new ResponseEntity<>(addressService.save(jwtToken, id, addressDto), HttpStatus.CREATED);
    }
}
