package io.elice.shoppingmall.member.repository;

import io.elice.shoppingmall.member.entity.LoginInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
    public boolean existsByEmail(String email);
    public Optional<LoginInfo> findByProviderId(String providerId);
}
