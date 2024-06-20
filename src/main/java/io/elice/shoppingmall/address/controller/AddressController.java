package io.elice.shoppingmall.address.controller;


import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.entity.AddressDTO;
import io.elice.shoppingmall.address.entity.AddressResponseDTO;
import io.elice.shoppingmall.address.service.AddressService;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원 주소 관리", description = "로그인된 회원의 주소 관련 API")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "회원의 모든 주소 조회", description = "현재 로그인된 회원이 등록한 모든 주소를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddressResponseDTO.class))))
    })
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressResponseDTO>> getMemberAddresses(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(addressService.findAllByUsernameToResponseDTO(userDetails.getUsername()), HttpStatus.OK);
    }

    @Operation(summary = "회원의 특정 주소 조회", description = "현재 로그인된 회원의 특정 주소를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AddressResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "주소를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/addresses/{id}")
    public ResponseEntity<AddressResponseDTO> getMemberAddress(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "id", description = "조회하려는 주소의 고유 번호", in = ParameterIn.PATH) @PathVariable Long id){
        return new ResponseEntity<>(addressService.findByUsernameAndAddressId(userDetails.getUsername(), id), HttpStatus.OK);
    }

    @Operation(summary = "회원의 특정 주소 수정", description = "현재 로그인된 회원의 특정 주소를 수정합니다. 만약 해당 주소가 존재하지 않으면 새로운 주소로 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = AddressResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/addresses/{id}")
    public ResponseEntity<AddressResponseDTO> postMemberAddress(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "id", description = "수정하려는 주소의 고유 번호", in = ParameterIn.PATH) @PathVariable Long id, @RequestBody AddressDTO addressDto){
        return new ResponseEntity<>(addressService.saveAndReturnResponseDTO(userDetails.getUsername(), id, addressDto), HttpStatus.OK);
    }

    @Operation(summary = "회원의 새로운 주소 등록", description = "현재 로그인된 회원의 새로운 주소를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = AddressResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/addresses")
    public ResponseEntity<AddressResponseDTO> updateMemberAddress(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddressDTO addressDto){
        return new ResponseEntity<>(addressService.saveAndReturnResponseDTO(userDetails.getUsername(), addressDto), HttpStatus.CREATED);
    }

    @Operation(summary = "회원의 특정 주소 삭제", description = "현재 로그인된 회원의 특정 주소를 삭제합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = AddressResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<String> deleteMemberAddress(@AuthenticationPrincipal UserDetails userDetails,
        @Parameter(name = "id", description = "삭제하려는 주소의 고유 번호", in = ParameterIn.PATH) @PathVariable Long id){
        return new ResponseEntity<>(addressService.delete(userDetails.getUsername(), id), HttpStatus.OK);
    }
}
