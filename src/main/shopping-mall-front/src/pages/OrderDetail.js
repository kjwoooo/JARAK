import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { apiInstance } from '../util/api';
import useUserStore from '../stores/useUserStore';
import { toast } from 'react-toastify';
import { Button } from 'react-bootstrap';
import OrderEdit from '../OrderEdit';
import './OrderDetail.css';

const OrderDetail = () => {
    const { orderId } = useParams();
    const [order, setOrder] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);
    const [itemNames, setItemNames] = useState({}); // itemName 상태 추가
    const [itemImages, setItemImages] = useState({});
    const user = useUserStore(state => state.user); // 현재 로그인한 사용자 정보

    const fetchOrderDetail = async () => {
        try {
            const response = await apiInstance.get(`/orders/order/${orderId}`);
            setOrder(response.data);
        } catch (error) {
            console.error('Failed to fetch order detail:', error);
        }
    };

    const fetchItemImages = async (itemId) => {
        try {
            const response = await apiInstance.get(`/items/${itemId}/itemimages`);
            setItemImages(prevState => ({
                ...prevState,
                [itemId]: response.data
            }));
        } catch (error) {
            console.error('Failed to fetch item images:', error);
        }
    };

    const fetchItemName = async (itemId) => {
        try {
            const response = await apiInstance.get(`/items/${itemId}`);
            return response.data.itemName;
        } catch (error) {
            console.error('Failed to fetch item name:', error);
            return 'Unknown Item';
        }
    };

    useEffect(() => {
        fetchOrderDetail();
    }, [orderId]);

    useEffect(() => {
        if (order && order.orderDetails) {
            order.orderDetails.forEach(orderDetail => {
                fetchItemImages(orderDetail.itemId);
            });
        }
    }, [order]);

    useEffect(() => {
        if (order && order.orderDetails) {
            const fetchNames = async () => {
                const names = await Promise.all(order.orderDetails.map(async (detail) => {
                    const name = await fetchItemName(detail.itemId);
                    return { [detail.itemId]: name };
                }));
                const namesMap = Object.assign({}, ...names);
                setItemNames(namesMap);
            };
            fetchNames();
        }
    }, [order]);

    const handleEditClick = () => {
        if (order.orderState === 'PENDING' || order.orderState === 'CONFIRMED') {
            setShowEditModal(true);
        } else if (order.orderState === 'SHIPPED' || order.orderState === 'DELIVERED') {
            toast.error('현재 상품이 배송되어 주소를 변경할 수 없습니다.');
        }
    };

    const getMainImageSrc = (itemId) => {
        const images = itemImages[itemId];
        if (!images) return '';
        const mainImage = images.find(image => image.isMain);
        return mainImage ? mainImage.filePath : '';
    };

    const closeEditModal = () => setShowEditModal(false);

    if (!order) {
        return <div>Loading...</div>;
    }

    const { orderDetails = [], shippingCost, price, recipientName, zipcode, addr, addrDetail, recipientTel, addrName, deliveryReq, orderState } = order;

    return (
        <div className="order-detail-container">
            <div className="order-detail-header">주문 상세 내역</div>

            <section className="order-detail-section product-info">
                <div className="section-title">상품 정보</div>
                {orderDetails.length > 0 ? (
                    orderDetails.map((orderDetail, index) => (
                        <div key={index} className="order-detail-item">
                            <img
                                src={getMainImageSrc(orderDetail.itemId)}
                                alt={itemNames[orderDetail.itemId] || '상품 이미지'}
                                className="order-detail-image"
                            />
                            <div className="order-detail-info">
                                <div className="item-name">{itemNames[orderDetail.itemId] || 'Unknown Item'}</div>
                                <div className="item-quantity">{orderDetail.price.toLocaleString()}원 / 수량 {orderDetail.quantity}개</div>
                                <div className="item-color">{orderDetail.color} / {orderDetail.size}</div>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="no-product-info">상품 정보가 없습니다.</div>
                )}
            </section>

            <section className="order-detail-section buyer-info">
                <div className="section-title">구매자 정보</div>
                <div className="buyer-info-details">
                    <div className="buyer-name">주문자: {user.displayName}</div>
                    <div className="buyer-phone">연락처: {user.phone}</div>
                    <div className="buyer-email">이메일: {user.loginInfo.email}</div>
                </div>
            </section>

            <section className="order-detail-section shipping-info">
                <div className="section-title">배송지 정보</div>
                <div className="shipping-info-details">
                    <div className="recipient-name">수령인: {recipientName}</div>
                    <div className="recipient-tel">연락처: {recipientTel}</div>
                    <div className="zipcode">우편번호: {zipcode}</div>
                    <div className="address">주소: {addr} {addrDetail}</div>
                    <div className="delivery-req">배송 요청 사항: {deliveryReq}</div>
                    {orderState !== 'CANCELLED' && (
                        <Button variant="primary" onClick={handleEditClick} className="order-edit-button">배송지 수정</Button>
                    )}
                </div>
            </section>

            <section className="order-detail-section payment-info">
                <div className="section-title">주문 금액 상세</div>
                <div className="payment-info-details">
                    <div className="total-price">주문 금액: {price.toLocaleString()} 원</div>
                    <div className="item-price">상품 금액: {(price - shippingCost).toLocaleString()} 원</div>
                    <div className="shipping-cost">배송비: {shippingCost.toLocaleString()} 원</div>
                </div>
            </section>

            <OrderEdit
                show={showEditModal}
                handleClose={closeEditModal}
                orderId={orderId}
                initialData={{
                    recipientName,
                    addrName,
                    recipientTel,
                    zipcode,
                    addr,
                    addrDetail,
                    deliveryReq,
                    shippingCost
                }}
                onSave={fetchOrderDetail}
            />
        </div>
    );
};

export default OrderDetail;
