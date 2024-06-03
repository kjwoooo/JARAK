package io.elice.shoppingmall.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "order_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Column(name = "pay_card", length = 255, nullable = false)
    private String payCard;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state", nullable = false)
    private OrderState orderState;

    @Column(name = "refund_reason", length = 255)
    private String refundReason;

    @Column(name = "recipient_name", length = 255, nullable = false)
    private String recipientName;

    @Column(name = "zipcode", length = 20, nullable = false)
    private String zipcode;

    @Column(name = "addr", length = 255, nullable = false)
    private String addr;

    @Column(name = "addr_detail", length = 255)
    private String addrDetail;

    @Column(name = "recipient_tel", length = 20, nullable = false)
    private String recipientTel;

    @Column(name = "delivery_req", length = 255)
    private String deliveryReq;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "rep_item_name", length = 255, nullable = false)
    private String repItemName;

    @Column(name = "rep_item_image", length = 255)
    private String repItemImage;
}
