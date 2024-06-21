import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { apiInstance } from '../util/api';
import './OrderDetail.css';

const OrderDetail = () => {
    const { orderId } = useParams();
    const [order, setOrder] = useState(null);
    const [itemImages, setItemImages] = useState({});

    useEffect(() => {
        const fetchOrderDetail = async () => {
            try {
                const response = await apiInstance.get(`/orders/order/${orderId}`);
                console.log('Fetched order details:', response.data); // orderDetails 확인을 위한 로그 추가
                setOrder(response.data);
            } catch (error) {
                console.error('Failed to fetch order detail:', error);
            }
        };

        fetchOrderDetail();
    }, [orderId]);

    if (!order) {
        return <div>Loading...</div>;
    }

    const { orderDetails, member, shippingCost, price, recipientName, zipcode, addr, addrDetail, recipientTel, addrName, deliveryReq } = order;

    const getMainImageSrc = (itemId) => {
        const images = itemImages[itemId];
        if (!images) return '';
        const mainImage = images.find(image => image.isMain);
        return mainImage ? mainImage.filePath : '';
    };

    console.log('orderDetails:', orderDetails); // orderDetails 로그 추가

    return (
        <div className="order-detail-container">
            <h1>주문 상세 내역</h1>

            <section className="order-detail-section">
                <h2>상품 정보</h2>
                {orderDetails.map((orderDetail, index) => (
                    <div key={index} className="order-detail-item">
                        <img src={getMainImageSrc(orderDetail.item.itemId)} alt={orderDetail.item.itemName} className="order-detail-image" />
                        <div className="order-detail-info">
                            <p>상품: {orderDetail.item.itemName}</p>
                            <p>가격: {orderDetail.price.toLocaleString()} 원</p>
                            <p>수량: {orderDetail.quantity}</p>
                            <p>색상: {orderDetail.color}</p>
                            <p>사이즈:{orderDetail.size}</p>
                        </div>
                    </div>
                ))}
            </section>

            <section className="order-detail-section">
                <h2>구매자 정보</h2>
                <p>주문자: {member.name}</p>
                <p>연락처: {member.phone}</p>
                <p>이메일: {member.email}</p>
            </section>

            <section className="order-detail-section">
                <h2>배송지 정보</h2>
                <p>수령인: {recipientName}</p>
                <p>연락처: {recipientTel}</p>
                <p>배송지명: {addrName}</p>
                <p>우편번호: {zipcode}</p>
                <p>주소: {addr} {addrDetail}</p>
                <p>배송 요청 사항: {deliveryReq}</p>
            </section>

            <section className="order-detail-section">
                <h2>주문 금액 상세</h2>
                <p>총 주문 금액: {price.toLocaleString()} 원</p>
                <p>상품 금액: {(price - shippingCost).toLocaleString()} 원</p>
                <p>배송비: {shippingCost.toLocaleString()} 원</p>
                <p>총 결제 금액: {price.toLocaleString()} 원</p>
            </section>
        </div>
    );
};

export default OrderDetail;

