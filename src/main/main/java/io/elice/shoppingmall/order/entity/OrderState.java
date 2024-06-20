package io.elice.shoppingmall.order.entity;

public enum OrderState {
    PENDING,      // 주문 생성됨
    CONFIRMED,    // 주문 확인됨
    SHIPPED,      // 주문 발송됨
    DELIVERED,    // 주문 고객에게 전달됨
    CANCELLED     // 주문 취소됨
}
