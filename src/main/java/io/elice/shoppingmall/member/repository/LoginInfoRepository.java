package io.elice.shoppingmall.member.repository;

import io.elice.shoppingmall.member.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

}
