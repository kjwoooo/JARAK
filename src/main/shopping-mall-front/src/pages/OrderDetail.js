import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { apiInstance } from '../util/api';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import './OrderDetail.css'; // 주문 상세 내역 페이지의 스타일

const OrderDetail = () => {
    const { orderId } = useParams();
    const [orderDetail, setOrderDetail] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchOrderDetail = async () => {
            try {
                const response = await apiInstance.get(`/orders/order/${orderId}`);
                setOrderDetail(response.data);
                setLoading(false);
            } catch (error) {
                setError(error);
                setLoading(false);
            }
        };

        fetchOrderDetail();
    }, [orderId]);

    if (loading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p>Error loading order detail: {error.message}</p>;
    }

    if (!orderDetail) {
        return <p>No order detail available.</p>;
    }

    return (
        <Container className="order-detail-container mt-4">
            <Card>
                <Card.Header>
                    <h2>Order Detail for Order #{orderDetail.id}</h2>
                </Card.Header>
                <Card.Body>
                    <Row>
                        <Col md={6}>
                            <h4>Order Information</h4>
                            <p><strong>Order ID:</strong> {orderDetail.id}</p>
                            <p><strong>Status:</strong> {orderDetail.orderState}</p>
                            <p><strong>Total Price:</strong> {orderDetail.price.toLocaleString()}</p>
                            <p><strong>Shipping Cost:</strong> {orderDetail.shippingCost.toLocaleString()}</p>
                            <p><strong>Order Date:</strong> {new Date(orderDetail.createdAt).toLocaleDateString('ko-KR')}</p>
                        </Col>
                        <Col md={6}>
                            <h4>Recipient Information</h4>
                            <p><strong>Name:</strong> {orderDetail.recipientName}</p>
                            <p><strong>Phone:</strong> {orderDetail.recipientTel}</p>
                            <p><strong>Address:</strong> {orderDetail.addr} {orderDetail.addrDetail}</p>
                            <p><strong>Zipcode:</strong> {orderDetail.zipcode}</p>
                        </Col>
                    </Row>
                    <h4 className="mt-4">Items</h4>
                    {orderDetail.orderDetails.map(item => (
                        <Card key={item.id} className="mb-3">
                            <Card.Body>
                                <Row>
                                    <Col md={6}>
                                        <p><strong>Item ID:</strong> {item.item.id}</p>
                                        <p><strong>Item Name:</strong> {item.item.name}</p>
                                        <p><strong>Quantity:</strong> {item.quantity}</p>
                                        <p><strong>Price:</strong> {item.price.toLocaleString()}</p>
                                    </Col>
                                    <Col md={6}>
                                        <p><strong>Color:</strong> {item.color}</p>
                                        <p><strong>Size:</strong> {item.size}</p>
                                        <p><strong>Status:</strong> {item.orderState}</p>
                                    </Col>
                                </Row>
                            </Card.Body>
                        </Card>
                    ))}
                    <Button variant="primary" onClick={() => window.history.back()}>Back</Button>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default OrderDetail;
