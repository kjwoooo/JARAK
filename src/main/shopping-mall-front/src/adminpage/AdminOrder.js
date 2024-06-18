import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Button, Modal, Form } from 'react-bootstrap';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import './AdminOrder.css';
import moment from "moment/moment";

const AdminOrder = () => {
    const [orders, setOrders] = useState([]);
    const [summary, setSummary] = useState({
        ordersCount: 0,
        prepareCount: 0,
        deliveryCount: 0,
        completeCount: 0,
    });
    const [showModal, setShowModal] = useState(false);
    const [orderIdToDelete, setOrderIdToDelete] = useState(null);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const orderStateMap = {
        PENDING: "주문 완료",
        CONFIRMED: "주문 확인",
        SHIPPED: "상품 발송",
        DELIVERED: "배송 완료",
        CANCELLED: "주문 취소"
    };

    useEffect(() => {
        fetchOrders();
    }, [page]);

    const fetchOrders = async () => {
        try {
            const response = await axios.get('/orders', {
                params: {
                    page: page,
                    size: 10,
                },
            });
            setOrders(response.data.content);
            setTotalPages(response.data.totalPages);

            const summaryData = {
                ordersCount: response.data.totalElements,
                prepareCount: response.data.content.filter(order => orderStateMap[order.orderState] === '주문 확인').length,
                deliveryCount: response.data.content.filter(order => orderStateMap[order.orderState] === '상품 발송').length,
                completeCount: response.data.content.filter(order => orderStateMap[order.orderState] === '배송 완료').length,
            };
            setSummary(summaryData);
        } catch (error) {
            console.error('Failed to fetch orders:', error);
        }
    };

    const handleStatusChange = async (id, status) => {
        try {
            await axios.patch(`/admin/orders/${id}`, { status });
            fetchOrders();
        } catch (error) {
            console.error('Failed to update status:', error);
        }
    };

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
            await axios.delete(`/admin/orders/${orderIdToDelete}`);
            setOrders(orders.filter(order => order.id !== orderIdToDelete));
            closeModal();
        } catch (error) {
            console.error('Failed to delete order:', error);
        }
    };

    const handlePageChange = (newPage) => {
        setPage(newPage);
    };

    return (
        <div>
            <Container className="section mt-4">
                <div className="block account-header">
                    <h1 className="subtitle is-4">주문 관리</h1>
                </div>

                <nav className="level">
                    <div className="level-item has-text-centered">
                        <div>
                            <p className="heading">총 주문 수</p>
                            <p className="title" id="ordersCount">{summary.ordersCount}</p>
                        </div>
                    </div>
                    <div className="level-item has-text-centered">
                        <div>
                            <p className="heading">상품 준비 중</p>
                            <p className="title" id="prepareCount">{summary.prepareCount}</p>
                        </div>
                    </div>
                    <div className="level-item has-text-centered">
                        <div>
                            <p className="heading">상품 배송 중</p>
                            <p className="title" id="deliveryCount">{summary.deliveryCount}</p>
                        </div>
                    </div>
                    <div className="level-item has-text-centered">
                        <div>
                            <p className="heading">배송 완료</p>
                            <p className="title" id="completeCount">{summary.completeCount}</p>
                        </div>
                    </div>
                </nav>

                <Container className="orders-container">
                    <Row className="notification is-info is-light is-mobile orders-top">
                        <Col md={2}>날짜</Col>
                        <Col md={4}>주문 정보</Col>
                        <Col md={2}>주문 총액</Col>
                        <Col md={2}>상태 관리</Col>
                        <Col md={2}>취소</Col>
                    </Row>
                    {orders.map(order => (
                        <Row className="orders-item" key={order.id}>
                            <Col md={2}>
                                {moment(order.createdAt).isValid()
                                    ? moment(order.createdAt).format('YYYY-MM-DD')
                                    : 'Invalid Date'}
                            </Col>
                            <Col md={4} className="order-summary">
                                {order.totalQuantity - 1 === 0 ? order.repItemName : `${order.repItemName} 외 ${order.totalQuantity - 1}건`}
                            </Col>
                            <Col md={2}>{order.totalPrice.toLocaleString()}</Col>
                            <Col md={2}>
                                <Form.Control
                                    as="select"
                                    value={order.orderState}
                                    onChange={(e) => handleStatusChange(order.id, e.target.value)}
                                    className={`status-select ${order.orderState.replace(" ", "-")}`}
                                >
                                    <option className="has-background-danger-light has-text-danger" value="상품 준비중">상품 준비 중</option>
                                    <option className="has-background-primary-light has-text-primary" value="상품 배송중">상품 배송 중</option>
                                    <option className="has-background-grey-light" value="배송완료">배송 완료</option>
                                </Form.Control>
                            </Col>
                            <Col md={2}>
                                <Button variant="danger" onClick={() => openModal(order.id)}>주문 취소</Button>
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

            <Modal show={showModal} onHide={closeModal}>
                <Modal.Header closeButton>
                    <Modal.Title>주문 취소 확인</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>주문 삭제 시 복구할 수 없습니다. 정말로 취소하시겠습니까?</p>
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

export default AdminOrder;
