package io.elice.shoppingmall.address.service;

import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.entity.AddressDTO;
import io.elice.shoppingmall.address.entity.AddressResponseDTO;
import io.elice.shoppingmall.address.repository.AddressRepository;
import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.repository.MemberRepository;
import io.elice.shoppingmall.member.service.MemberService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public List<Address> findAll(){
        return addressRepository.findAll();
    }

    public Address findById(Long id){
        return addressRepository.findById(id).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_ADDRESS));
    }

    /**
     * jwtToken으로 인증된 회원의 모든 주소 검색
     * @param jwtToken
     * @return
     */
    public List<AddressResponseDTO> findAllByJwtToken(String jwtToken){
        Member member = memberService.findByJwtToken(jwtToken);

        return addressRepository.findByMember(member).stream().map(AddressResponseDTO::new).toList();
    }

    /**
     * jwtToken으로 인증된 회원의 특정 주소 검색
     * @param jwtToken
     * @param addressId
     * @return
     */
    public AddressResponseDTO findByJwtTokenAndAddressId(String jwtToken, Long addressId){
        Member member = memberService.findByJwtToken(jwtToken);
        Address address = addressRepository.findByIdAndMember(addressId, member).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_ADDRESS));
        return new AddressResponseDTO(address);
    }

    public void delete(Long id){
        addressRepository.delete(findById(id));
    }

    /**
     * DB에 주소 저장
     * @param address
     * @return
     */
    private Address save(Address address){
        return addressRepository.save(address);
    }

    /**
     * 인증받은 회원의 새로운 주소 등록
     * @param jwtToken
     * @param addressDto
     * @return
     */
    public Address save(String jwtToken,AddressDTO addressDto){
        Member member = memberService.findByJwtToken(jwtToken);

        Address address = addressDto.toEntity();
        address.setMember(member);

        return save(address);
    }

    public AddressResponseDTO saveAndReturnResponseDTO(String jwtToken, AddressDTO addressDTO){
        return new AddressResponseDTO(save(jwtToken, addressDTO));
    }

    /**
     * 인증받은 회원의 특정 주소 수정
     * @param jwtToken
     * @param id
     * @param addressDTO
     * @return
     */
    public Address save(String jwtToken, Long id, AddressDTO addressDTO){
        try{
            Address oldAddress = findById(id);
            Address newAddress = addressDTO.toEntity();

            Member member = memberService.findByJwtToken(jwtToken);

            newAddress.setMember(member);
            newAddress.setId(oldAddress.getId());

            return save(newAddress);

        } catch(CustomException e){
            if(e.getCode() == ErrorCode.NOT_FOUND_ADDRESS)
                return save(jwtToken, addressDTO);
            else
                throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }
    }

    public AddressResponseDTO saveAndReturnResponseDTO(String jwtToken, Long id, AddressDTO addressDTO){
        return new AddressResponseDTO(save(jwtToken, addressDTO));
    }
}
