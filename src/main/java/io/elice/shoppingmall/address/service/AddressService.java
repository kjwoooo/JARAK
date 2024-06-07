package io.elice.shoppingmall.address.service;

import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.entity.AddressDTO;
import io.elice.shoppingmall.address.entity.AddressResponseDTO;
import io.elice.shoppingmall.address.repository.AddressRepository;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    public List<Address> findAll(){
        return addressRepository.findAll();
    }

    public Optional<Address> findById(Long id){
        return addressRepository.findById(id);
    }

    public List<Address> findByMemberId(Long memberId){
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null){
            return null;
        }

        return addressRepository.findByMember(member);
    }

    public Optional<Address> findByAddressIdAndMemberId(Long memberId, Long addressId){
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if(memberOptional.isEmpty()){
            return Optional.empty();
        }

        return addressRepository.findByIdAndMember(addressId, memberOptional.get());
    }

    public void delete(Long id){
        Optional<Address> addressOptional = addressRepository.findById(id);

        if(addressOptional.isEmpty())
            return;

        addressRepository.delete(addressOptional.get());
    }

    private Optional<AddressResponseDTO> save(Address address){
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO(addressRepository.save(address));
        return Optional.of(addressResponseDTO);
    }

    public Optional<AddressResponseDTO> save(AddressDTO addressDto){
        Optional<Member> memberOptional = memberRepository.findById(addressDto.getMemberId());

        if(memberOptional.isEmpty()){
            return Optional.empty();
        }

        Address address = addressDto.toEntity();
        address.setMember(memberOptional.get());

        return save(address);
    }

    public Optional<AddressResponseDTO> save(Long id, AddressDTO addressDTO){
        Optional<Address> addressOptional = addressRepository.findById(id);

        if(addressOptional.isEmpty()){
            return save(addressDTO);
        }

        Address address = addressOptional.get();
        Address newAddress = addressDTO.toEntity();

        newAddress.setMember(address.getMember());
        newAddress.setId(address.getId());

        return save(newAddress);
    }
}
