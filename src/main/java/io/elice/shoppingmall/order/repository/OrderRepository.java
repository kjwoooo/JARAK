package io.elice.shoppingmall.order.repository;

import io.elice.shoppingmall.order.entity.Order;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 특정 사용자에 대한 주문 조회
    Page<Order> findByMemberIdOrderByIdDesc(Long memberId, Pageable pageable);
}