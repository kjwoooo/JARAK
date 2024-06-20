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
            const response = await axios.get('/admin/orders', {
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
            await axios.put(`/admin/orders/${id}`, { status });
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
            fetchOrders();
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
            <Container className="admin-section-unique">
                <div className="admin-block-unique admin-account-header-unique">
                    <h1 className="admin-subtitle-unique is-4">주문 관리</h1>
                </div>

                <nav className="admin-level-unique">
                    <div className="admin-level-item-unique has-text-centered">
                        <div>
                            <p className="admin-heading-unique">총 주문 수</p>
                            <p className="admin-title-unique" id="ordersCount">{summary.ordersCount}</p>
                        </div>
                    </div>
                    <div className="admin-level-item-unique has-text-centered">
                        <div>
                            <p className="admin-heading-unique">상품 준비 중</p>
                            <p className="admin-title-unique" id="prepareCount">{summary.prepareCount}</p>
                        </div>
                    </div>
                    <div className="admin-level-item-unique has-text-centered">
                        <div>
                            <p className="admin-heading-unique">상품 배송 중</p>
                            <p className="admin-title-unique" id="deliveryCount">{summary.deliveryCount}</p>
                        </div>
                    </div>
                    <div className="admin-level-item-unique has-text-centered">
                        <div>
                            <p className="admin-heading-unique">배송 완료</p>
                            <p className="admin-title-unique" id="completeCount">{summary.completeCount}</p>
                        </div>
                    </div>
                </nav>

                <Container className="admin-orders-container-unique">
                    <div className="admin-order-orders-top">
                        <div className="admin-order-col-2">날짜</div>
                        <div className="admin-order-col-4">주문 정보</div>
                        <div className="admin-order-col-2">주문 총액</div>
                        <div className="admin-order-col-2">상태 관리</div>
                        <div className="admin-order-col-2">취소</div>
                    </div>
                    {orders.map(order => (
                        <div
                            className={`admin-order-orders-item ${order.orderState === 'CANCELLED' ? 'admin-cancelled-unique' : ''}`}
                            key={order.id}>
                            <div className="admin-order-col admin-order-col-2">
                                {moment(order.createdAt).isValid()
                                    ? moment(order.createdAt).format('YYYY-MM-DD')
                                    : 'Invalid Date'}
                            </div>
                            <div className="admin-order-col admin-order-col-6 admin-order-summary-unique">
                                {order.totalQuantity - 1 === 0 ? order.repItemName : `${order.repItemName} 외 ${order.totalQuantity - 1}건`}
                            </div>
                            <div
                                className="admin-order-col admin-order-col-2">{order.totalPrice ? order.totalPrice.toLocaleString() : 'N/A'}</div>
                            <div className="admin-order-col admin-order-col-2">
                                <select
                                    value={order.orderState}
                                    onChange={(e) => handleStatusChange(order.id, e.target.value)}
                                    className={`admin-status-select-unique ${order.orderState.replace(" ", "-")}`}
                                >
                                    <option value="상품 준비중">상품 준비 중</option>
                                    <option value="상품 배송중">상품 배송 중</option>
                                    <option value="배송완료">배송 완료</option>
                                </select>
                            </div>
                            <div className="admin-order-col admin-order-col-2 admin-order-cancel-button-container">
                                <button
                                    className="admin-order-cancel-button"
                                    onClick={() => openModal(order.id)}
                                    disabled={order.orderState === 'CANCELLED'}
                                >
                                    주문 취소
                                </button>
                            </div>
                        </div>
                    ))}
                    <div className="admin-order-pagination">
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
