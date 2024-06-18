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
     * @deprecated findAllByUsername 사용 권장
     * @param jwtToken
     * @return
     */
    @Deprecated
    public List<Address> findAllByJwtToken(String jwtToken){
        Member member = memberService.findByJwtToken(jwtToken);

        return addressRepository.findByMember(member);
    }

    public List<Address> findAllByUsername(String username){
        Member member = memberService.findByUsername(username);

        return addressRepository.findByMember(member);
    }

    public List<Address> findAllByMember(Member member){
        return addressRepository.findByMember(member);
    }

    /**
     * @deprecated findAllByUsernameToResponseDTO 사용 권장
     * @param jwtToken
     * @return
     */
    @Deprecated
    public List<AddressResponseDTO> findAllByJwtTokenAndReturnResponseDTO(String jwtToken){
        return findAllByJwtToken(jwtToken).stream().map(AddressResponseDTO::new).toList();

    }

    public List<AddressResponseDTO> findAllByUsernameToResponseDTO(String username){
        return findAllByUsername(username).stream().map(AddressResponseDTO::new).toList();
    }

    /**
     * @deprecated findByUsernameAndAddressId 사용 권장
     * @param jwtToken
     * @param addressId
     * @return
     */
    @Deprecated
    public AddressResponseDTO findByJwtTokenAndAddressId(String jwtToken, Long addressId){
        Member member = memberService.findByJwtToken(jwtToken);
        Address address = addressRepository.findByIdAndMember(addressId, member).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_ADDRESS));
        return new AddressResponseDTO(address);
    }

    public AddressResponseDTO findByUsernameAndAddressId(String username, Long addressId){
        Member member = memberService.findByUsername(username);
        Address address = addressRepository.findByIdAndMember(addressId, member).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_ADDRESS));

        return new AddressResponseDTO(address);
    }

    public String delete(String username, Long id){
        Member member = memberService.findByUsername(username);
        Address address = addressRepository.findById(id).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_ADDRESS));

        if(address.getMember().getId() == member.getId()){
            addressRepository.delete(findById(id));
            return "주소 삭제";
        }

        throw new CustomException(ErrorCode.NOT_MATCHE_ADDRESS_TO_MEMBER);
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
     * @param member
     * @param addressDto
     * @return
     */
    public Address save(Member member,AddressDTO addressDto){
        if(member == null)
            throw new CustomException(ErrorCode.NULL_POINT_MEMBER);

        Address address = addressDto.toEntity();
        address.setMember(member);

        return save(address);
    }

    public AddressResponseDTO saveAndReturnResponseDTO(String username, AddressDTO addressDTO){
        Member member = memberService.findByUsername(username);
        return new AddressResponseDTO(save(member, addressDTO));
    }

    /**
     * 인증받은 회원의 특정 주소 수정
     * @param username
     * @param id
     * @param addressDTO
     * @return
     */
    public Address save(String username, Long id, AddressDTO addressDTO){
        Member member = memberService.findByUsername(username);
        Address[] addresses = new Address[1];

        addressRepository.findById(id).ifPresentOrElse(oldAddress -> {
            Address newAddress = addressDTO.toEntity();
            newAddress.setMember(member);
            newAddress.setId(oldAddress.getId());

            addresses[0] = save(newAddress);
        },() -> save(member, addressDTO));

        return addresses[0];
    }

    public AddressResponseDTO saveAndReturnResponseDTO(String username, Long id, AddressDTO addressDTO){
        return new AddressResponseDTO(save(username, id, addressDTO));
    }
}
