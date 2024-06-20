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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<List<AddressResponseDTO>> getMemberAddresses(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(addressService.findAllByUsernameToResponseDTO(userDetails.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<AddressResponseDTO> getMemberAddress(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id){
        return new ResponseEntity<>(addressService.findByUsernameAndAddressId(userDetails.getUsername(), id), HttpStatus.OK);
    }

    @PostMapping("/addresses/{id}")
    public ResponseEntity<AddressResponseDTO> postMemberAddress(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody AddressDTO addressDto){
        return new ResponseEntity<>(addressService.saveAndReturnResponseDTO(userDetails.getUsername(), id, addressDto), HttpStatus.OK);
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressResponseDTO> updateMemberAddress(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddressDTO addressDto){
        return new ResponseEntity<>(addressService.saveAndReturnResponseDTO(userDetails.getUsername(), addressDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<String> deleteMemberAddress(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id){
        return new ResponseEntity<>(addressService.delete(userDetails.getUsername(), id), HttpStatus.OK);
    }
}
