import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Button, Modal } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import moment from 'moment';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Orders.css';

const Orders = () => {
    const [orders, setOrders] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [orderIdToDelete, setOrderIdToDelete] = useState(null);
    const [page, setPage] = useState(0); // 현재 페이지 번호
    const [totalPages, setTotalPages] = useState(1); // 전체 페이지 수
    const navigate = useNavigate();
    const orderStateMap = {
        PENDING: "주문 완료",
        CONFIRMED: "주문 확인",
        SHIPPED: "상품 발송",
        DELIVERED: "배송 완료",
        CANCELLED: "주문 취소"
    };

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const response = await axios.get('/orders', {
                    params: {
                        page: page,
                        size: 10
                    }
                });
                setOrders(response.data.content);
                setTotalPages(response.data.totalPages);
            } catch (error) {
                console.error('Failed to fetch orders:', error);
            }
        };

        fetchOrders();
    }, [page]);

    const openModal = (id) => {
        setOrderIdToDelete(id);
        setShowModal(true);
    };

    const closeModal = () => {
        setShowModal(false);
        setOrderIdToDelete(null);
    };

    const deleteOrderData = async () => {
        try {
            await axios.delete(`/orders/${orderIdToDelete}`);
            setOrders(orders.filter(order => order.id !== orderIdToDelete));
            closeModal();
        } catch (error) {
            console.error('Failed to delete order:', error);
        }
    };

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < totalPages) {
            setPage(newPage);
        }
    };

    return (
        <div>
            <section className="section">
                <Container>
                    <div className="block account-header">
                        <h1 className="subtitle is-4">주문조회</h1>
                    </div>
                    <Container className="orders-container">
                        <Row className="notification is-info is-light is-mobile orders-top">
                            <Col md={2}>날짜</Col>
                            <Col md={6}>주문정보</Col>
                            <Col md={2}>상태</Col>
                            <Col md={2}>신청</Col>
                        </Row>
                        {orders.map(orders => (
                            <Row className="orders-item" key={orders.id}>
                                <Col md={2}>
                                    {moment(orders.createdAt).isValid()
                                        ? moment(orders.createdAt).format('YYYY-MM-DD')
                                        : 'Invalid Date'}
                                </Col>

                                <Col md={6} className="order-summary">
                                    {orders.totalQuantity - 1 === 0 ? orders.repItemName : `${orders.repItemName} 외 ${orders.totalQuantity - 1}건`}
                                </Col>
                                <Col md={2}>{orderStateMap[orders.orderState]}</Col>
                                <Col md={2}>
                                    <Button variant="danger" onClick={() => openModal(orders.id)}>주문 취소</Button>
                                </Col>
                            </Row>
                        ))}
                        <div className="pagination">
                            <Button onClick={() => handlePageChange(page - 1)} disabled={page === 0}>
                                이전
                            </Button>
                            <span>{page + 1} / {totalPages}</span>
                            <Button onClick={() => handlePageChange(page + 1)} disabled={page === totalPages - 1}>
                                다음
                            </Button>
                        </div>
                    </Container>
                </Container>
            </section>

            <Modal show={showModal} onHide={closeModal}>
                <Modal.Header closeButton>
                    <Modal.Title>주문 취소 확인</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>주문 취소 시 복구할 수 없습니다. 정말로 취소하시겠습니까?</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        아니오
                    </Button>
                    <Button variant="danger" onClick={deleteOrderData}>
                        네
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default Orders;
