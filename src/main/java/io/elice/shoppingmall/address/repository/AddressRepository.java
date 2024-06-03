package io.elice.shoppingmall.address.repository;

import io.elice.shoppingmall.address.entity.Address;
import io.elice.shoppingmall.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    public Optional<Address> findByMember(Member member);
}
