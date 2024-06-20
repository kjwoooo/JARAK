package io.elice.shoppingmall.order.repository;

import io.elice.shoppingmall.order.entity.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 특정 사용자에 대한 주문 페이징 처리하여 조회
    Page<Order> findByMemberIdOrderByIdDesc(Long memberId, Pageable pageable);

    // 주문 id와 사용자 id로 주문 상세 내역 조회
    Optional<Order> findByIdAndMemberId(Long id, Long memberId);

    // 사용자 이름에 해당하는 주문을 페이징 처리하여 검색
    Page<Order> findByMemberUsernameContaining(String username, Pageable pageable);
}