package io.elice.shoppingmall.order.entity;

public enum OrderState {
    PENDING,      // 주문 완료
    CONFIRMED,    // 주문 확인
    SHIPPED,      // 상품 발송
    DELIVERED,    // 배송 완료
    CANCELLED     // 주문 취소
}
