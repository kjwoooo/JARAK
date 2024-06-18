import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import './OrderComplete.css';

const OrderComplete = () => {
    const navigate = useNavigate();

    const handleOrderDetailClick = () => {
        navigate('/mypage/orders');
    };

    const handleShoppingClick = () => {
        navigate('/');
    };

    return (
        <div>
            <Container className="section">
                {/*<Row className="cart-header">*/}
                {/*    <Col>*/}
                {/*        <p className="is-size-6">장바구니 &gt; 주문결제 &gt;</p>*/}
                {/*    </Col>*/}
                {/*    <Col>*/}
                {/*        <h2 className="is-size-2">주문완료</h2>*/}
                {/*    </Col>*/}
                {/*</Row>*/}

                <Row className="justify-content-center order-complete-container">
                    <Col md={6}>
                        <Card className="text-center order-complete">
                            <Card.Body>
                                <div className="icon-text">
                                    <i className="fas fa-circle-check text-success fa-3x"></i>
                                    <Card.Title>주문이 완료되었습니다!</Card.Title>
                                </div>
                                <div className="buttons-container">
                                    <Button
                                        variant="light"
                                        size="lg"
                                        block
                                        className="order-detail-button"
                                        onClick={handleOrderDetailClick}
                                    >
                                        주문내역 보기
                                    </Button>
                                    <Button
                                        variant="primary"
                                        size="lg"
                                        block
                                        className="order-button"
                                        onClick={handleShoppingClick}
                                    >
                                        쇼핑 계속하기
                                    </Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default OrderComplete;
