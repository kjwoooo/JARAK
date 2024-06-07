package io.elice.shoppingmall.member.repository;

import io.elice.shoppingmall.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findByUsername(String username);
    public boolean existsByUsername(String username);
}
