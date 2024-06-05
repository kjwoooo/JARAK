package io.elice.shoppingmall.address.service;

import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.address.entity.AddressDTO;
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
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member==null){
            return null;
        }

        return addressRepository.findByIdAndMember(addressId, member);
    }

    private Address save(Address address){
        return addressRepository.save(address);
    }

    public Address save(AddressDTO addressDto){
        Member member = memberRepository.findById(addressDto.getMemberId()).orElse(null);

        if(member == null){
            return null;
        }

        Address address = addressDto.toEntity();
        address.setMember(member);

        return save(address);
    }

    public Address save(Long id, AddressDTO addressDTO){
        Address address = addressRepository.findById(id).orElse(null);

        if(address == null){
            return save(addressDTO);
        }

        Address newAddress = addressDTO.toEntity();
        newAddress.setMember(address.getMember());
        newAddress.setId(address.getId());

        return save(newAddress);
    }
}
