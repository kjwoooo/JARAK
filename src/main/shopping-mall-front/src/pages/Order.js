/* global daum */
import React, { useState, useEffect } from 'react';
import { Form, Button, Modal, ListGroup } from 'react-bootstrap';
import { useLocation, useNavigate } from 'react-router-dom';
import useUserStore from '../stores/useUserStore.js';
import { apiInstance } from '../util/api.js';
import Cookies from 'js-cookie';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './Order.css';

function Order() {
    const location = useLocation();
    const navigate = useNavigate();
    const { cartItems = [], productTotal = 0, shipping = 0, total = 0 } = location.state || {};
    const user = useUserStore(state => state.user);
    const [showAddressModal, setShowAddressModal] = useState(false);
    const [showAddAddressModal, setShowAddAddressModal] = useState(false);
    const [showEditAddressModal, setShowEditAddressModal] = useState(false);
    const [editAddressId, setEditAddressId] = useState(null);
    const [customDeliveryReq, setCustomDeliveryReq] = useState(false);

    const jwtToken = Cookies.get('jwtToken'); // 쿠키에서 JWT 토큰 가져오기

    const [formData, setFormData] = useState({
        orderCustomer: user ? user.displayName : '',
        phone: '',
        address: '',
        deliveryReq: ''
    });
    const [addresses, setAddresses] = useState([]);
    const [newAddress, setNewAddress] = useState({
        recipientName: '', // 수령자
        addrName: '', // 배송지명
        recipientTel: '', // 휴대전화 번호
        zipcode: '', // 우편번호
        addr: '', // 주소
        addrDetail: '', // 상세주소
        deliveryReq: '' // 배송요청사항
    });

    useEffect(() => {
        if (user && jwtToken) {
            fetchAddresses();
        }
    }, [user, jwtToken]);

    const fetchAddresses = async () => {
        try {
            const response = await apiInstance.get('/addresses', { headers: { 'Authorization': `Bearer ${jwtToken}` } });
            console.log('Fetched addresses:', response.data); // 서버로부터 받아온 데이터 구조 확인
            setAddresses(Array.isArray(response.data) ? response.data : []);
        } catch (error) {
            console.error('Failed to fetch addresses:', error);
            setAddresses([]); // 오류 발생 시 빈 배열로 초기화
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleDeliveryReqChange = (e) => {
        const value = e.target.value;
        if (value === 'custom') {
            setCustomDeliveryReq(true);
            setFormData({
                ...formData,
                deliveryReq: ''
            });
        } else {
            setCustomDeliveryReq(false);
            setFormData({
                ...formData,
                deliveryReq: value
            });
        }
    };

    const handleNewAddressChange = (e) => {
        const { name, value } = e.target;
        setNewAddress({ ...newAddress, [name]: value });
    };

    const handleAddAddress = async () => {
        console.log('User:', user); // 유저 정보 확인
        console.log('JWT Token:', jwtToken); // JWT 토큰 확인
        if (!jwtToken || jwtToken.split('.').length !== 3) {
            console.error('Invalid JWT Token');
            return;
        }
        try {
            const response = await apiInstance.post('/addresses', newAddress, { headers: { 'Authorization': `Bearer ${jwtToken}` } });
            console.log('Added address:', response.data); // 추가된 데이터 구조 확인
            setAddresses([...addresses, response.data]);
            setNewAddress({
                recipientName: '',
                addrName: '',
                recipientTel: '',
                zipcode: '',
                addr: '',
                addrDetail: '',
                deliveryReq: ''
            });
            setShowAddAddressModal(false);
        } catch (error) {
            console.error('Failed to add address:', error);
        }
    };

    const handleEditAddressChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleEditAddress = async (address) => {
        setEditAddressId(address.id);
        try {
            const response = await apiInstance.get(`/addresses/${address.id}`, { headers: { 'Authorization': `Bearer ${jwtToken}` } });
            const addressData = response.data;
            setFormData({
                recipientName: addressData.recipientName,
                addrName: addressData.addrName,
                recipientTel: addressData.recipientTel,
                zipcode: addressData.zipcode,
                addr: addressData.addr,
                addrDetail: addressData.addrDetail,
                deliveryReq: addressData.deliveryReq
            });
            setShowEditAddressModal(true);
        } catch (error) {
            console.error('Failed to fetch address for editing:', error);
        }
    };

    const handleSaveEditedAddress = async () => {
        try {
            await apiInstance.post(`/addresses/${editAddressId}`, formData, { headers: { 'Authorization': `Bearer ${jwtToken}` } });
            fetchAddresses();
            setShowEditAddressModal(false);
        } catch (error) {
            console.error('Failed to edit address:', error);
        }
    };

    const handleDeleteAddress = async (id) => {
        const confirmed = window.confirm("정말 주소를 삭제하시겠습니까?");
        if (!confirmed) return;
        try {
            await apiInstance.delete(`/addresses/${id}`, { headers: { 'Authorization': `Bearer ${jwtToken}` } });
            setAddresses(addresses.filter(address => address.id !== id));
        } catch (error) {
            console.error('Failed to delete address:', error);
        }
    };

    const handleSelectAddress = (address) => {
        setFormData({
            ...formData,
            recipientName: address.recipientName,
            addrName: address.addrName,
            recipientTel: address.recipientTel,
            zipcode: address.zipcode,
            address: `${address.addr} ${address.addrDetail}`,
            deliveryReq: address.deliveryReq
        });
        setShowAddressModal(false);
    };

    const handlePostcode = () => {
        new daum.Postcode({
            oncomplete: function (data) {
                setNewAddress({
                    ...newAddress,
                    zipcode: data.zonecode,
                    addr: data.address
                });
            }
        }).open();
    };

    const handleCloseAddressModal = () => {
        setShowAddressModal(false);
        window.location.reload();
    };

    const handleSubmitOrder = async () => {
        const orderData = {
            shippingCost: shipping,
            recipientName: formData.recipientName,
            zipcode: formData.zipcode,
            addr: formData.addr,
            addrDetail: formData.addrDetail,
            recipientTel: formData.recipientTel,
            addrName: formData.addrName,
            deliveryReq: formData.deliveryReq
        };

        try {
            const response = await apiInstance.post('/orders', orderData, {
                headers: { 'Authorization': `Bearer ${jwtToken}` }
            });
            console.log('Order created:', response.data);
            toast.success('주문이 성공적으로 진행되었습니다!');
            navigate('/order/complete'); // 주문 내역 조회 페이지로 이동
        } catch (error) {
            console.error('Failed to create order:', error);
            toast.error('주문 생성에 실패했습니다. 다시 시도해 주세요.');
        }
    };

    return (
        <div className="order-container-unique">
            <h1>ORDER</h1>
            <div className="order-form-unique">
                <div className="order-shipping-info-unique">
                    <h2>배송 정보</h2>
                    <Form>
                        <Form.Group controlId="formOrderCustomer">
                            <Form.Label>주문고객</Form.Label>
                            <Form.Control type="text" name="orderCustomer" value={formData.orderCustomer} onChange={handleChange} />
                        </Form.Group>
                        <Form.Group controlId="formPhone">
                            <Form.Label>휴대폰 번호</Form.Label>
                            <Form.Control type="text" name="phone" value={formData.phone} onChange={handleChange} />
                        </Form.Group>
                        <Form.Group controlId="formAddrName">
                            <Form.Label>배송지명</Form.Label>
                            <Form.Control type="text" name="addrName" value={formData.addrName} onClick={() => setShowAddressModal(true)} placeholder="배송지 선택" readOnly />
                        </Form.Group>
                        <Form.Group controlId="formRecipientName">
                            <Form.Label>받는 분</Form.Label>
                            <Form.Control type="text" name="recipientName" value={formData.recipientName} onChange={handleChange} readOnly />
                        </Form.Group>
                        <Form.Group controlId="formRecipientTel">
                            <Form.Label>받는 분 휴대폰 번호</Form.Label>
                            <Form.Control type="text" name="recipientTel" value={formData.recipientTel} onChange={handleChange} readOnly />
                        </Form.Group>
                        <Form.Group controlId="formZipcode">
                            <Form.Label>우편번호</Form.Label>
                            <Form.Control type="text" name="zipcode" value={formData.zipcode} onChange={handleChange} readOnly />
                        </Form.Group>
                        <Form.Group controlId="formAddress">
                            <Form.Label>배송 주소</Form.Label>
                            <Form.Control type="text" name="address" value={formData.address} onChange={handleChange} readOnly />
                        </Form.Group>
                        <Form.Group controlId="formDeliveryReq">
                            <Form.Label>배송요청사항</Form.Label>
                            <Form.Control
                                as="select"
                                name="deliveryReq"
                                value={customDeliveryReq ? 'custom' : formData.deliveryReq}
                                onChange={handleDeliveryReqChange}
                            >
                                <option value="문 앞에 두고 가주세요">문 앞에 두고 가주세요</option>
                                <option value="경비실에 맡겨주세요">경비실에 맡겨주세요</option>
                                <option value="택배함에 넣어주세요">택배함에 넣어주세요</option>
                                <option value="custom">직접 입력</option>
                            </Form.Control>
                            {customDeliveryReq && (
                                <Form.Control
                                    type="text"
                                    name="deliveryReq"
                                    placeholder="배송 요청 사항을 입력하세요"
                                    value={formData.deliveryReq}
                                    onChange={handleChange}
                                />
                            )}
                        </Form.Group>
                    </Form>
                </div>
                <div className="order-payment-info-unique">
                    <h2>결제 정보</h2>
                    {cartItems?.map(item => (
                        <div key={item.id} className="order-product-item-unique">
                            <p className="order-product-title-unique">상품명: {item.title}</p>
                            <p className="order-product-price-unique">가격: {item.price?.toLocaleString()} 원</p>
                            <p className="order-product-quantity-unique">수량: {item.quantity}</p>
                        </div>
                    ))}
                    <p className="order-total-amount-unique">총 상품 금액: {productTotal.toLocaleString()} 원</p>
                    <p className="order-total-amount-unique">배송비: {shipping.toLocaleString()} 원</p>
                    <p className="order-total-amount-unique">총 결제 금액: {total.toLocaleString()} 원</p>
                    <button
                        className="custom-button-primary submit-order-button"
                        onClick={handleSubmitOrder}
                    >
                        결제하기
                    </button>
                </div>
            </div>

            {/* 주소선택화면 */}
            <Modal show={showAddressModal} onHide={handleCloseAddressModal}>
                <Modal.Header closeButton>
                    <Modal.Title>배송지 선택</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <ListGroup>
                        {addresses.length > 0 ? addresses.filter(address => {
                            console.log('Filtering address:', address);
                            return address.memberId === user.id;
                        }).map(address => (
                            <ListGroup.Item key={address.id} action onClick={() => handleSelectAddress(address)} className="d-flex justify-content-between align-items-center">
                                <div>
                                    <p><strong>{address.addrName}</strong></p>
                                    <p>{address.addr} {address.addrDetail}</p>
                                </div>
                                <div>
                                    <Button variant="secondary" size="sm" onClick={(e) => { e.stopPropagation(); handleEditAddress(address); }}>수정</Button>
                                    <Button variant="danger" size="sm" onClick={(e) => { e.stopPropagation(); handleDeleteAddress(address.id); }}>삭제</Button>
                                </div>
                            </ListGroup.Item>
                        )) : <p>저장된 주소가 없습니다.</p>}
                    </ListGroup>
                    <Button variant="secondary" onClick={() => setShowAddAddressModal(true)}>주소 추가</Button>
                </Modal.Body>
            </Modal>

            {/* 주소추가화면 */}
            <Modal show={showAddAddressModal} onHide={() => setShowAddAddressModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>주소 추가</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="formAddrName">
                            <Form.Label>배송지명</Form.Label>
                            <Form.Control type="text" name="addrName" value={newAddress.addrName} onChange={handleNewAddressChange} />
                        </Form.Group>
                        <Form.Group controlId="formRecipientName">
                            <Form.Label>수령자</Form.Label>
                            <Form.Control type="text" name="recipientName" value={newAddress.recipientName} onChange={handleNewAddressChange} />
                        </Form.Group>
                        <Form.Group controlId="formRecipientTel">
                            <Form.Label>휴대전화 번호</Form.Label>
                            <Form.Control type="text" name="recipientTel" value={newAddress.recipientTel} onChange={handleNewAddressChange} />
                        </Form.Group>
                        <Form.Group controlId="formZipcode">
                            <Form.Label>우편번호</Form.Label>
                            <div className="d-flex">
                            <Form.Control type="text" name="zipcode" value={newAddress.zipcode} onChange={handleNewAddressChange} readOnly className="order-custom-zipcode" />
                                <Button variant="secondary" onClick={handlePostcode}>주소찾기</Button>
                            </div>
                        </Form.Group>
                        <Form.Group controlId="formAddr">
                            <Form.Label>주소</Form.Label>
                            <Form.Control type="text" name="addr" value={newAddress.addr} onChange={handleNewAddressChange} readOnly />
                        </Form.Group>
                        <Form.Group controlId="formAddrDetail">
                            <Form.Label>상세주소</Form.Label>
                            <Form.Control type="text" name="addrDetail" value={newAddress.addrDetail} onChange={handleNewAddressChange} />
                        </Form.Group>
                        <Form.Group controlId="formDeliveryReq">
                            <Form.Label>배송요청사항</Form.Label>
                            <Form.Control type="text" name="deliveryReq" value={newAddress.deliveryReq} onChange={handleNewAddressChange} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleAddAddress}>저장</Button>
                </Modal.Footer>
            </Modal>

            {/* 주소수정화면 */}
            <Modal show={showEditAddressModal} onHide={() => setShowEditAddressModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>주소 수정</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="formRecipientName">
                            <Form.Label>수령자</Form.Label>
                            <Form.Control type="text" name="recipientName" value={formData.recipientName} onChange={handleEditAddressChange} />
                        </Form.Group>
                        <Form.Group controlId="formAddrName">
                            <Form.Label>배송지명</Form.Label>
                            <Form.Control type="text" name="addrName" value={formData.addrName} onChange={handleEditAddressChange} />
                        </Form.Group>
                        <Form.Group controlId="formRecipientTel">
                            <Form.Label>휴대전화 번호</Form.Label>
                            <Form.Control type="text" name="recipientTel" value={formData.recipientTel} onChange={handleEditAddressChange} />
                        </Form.Group>
                        <Form.Group controlId="formZipcode">
                            <Form.Label>우편번호</Form.Label>
                            <div className="d-flex">
                                <Form.Control type="text" name="zipcode" value={formData.zipcode} onChange={handleEditAddressChange} readOnly />
                                <Button variant="secondary" onClick={handlePostcode}>주소찾기</Button>
                            </div>
                        </Form.Group>
                        <Form.Group controlId="formAddr">
                            <Form.Label>주소</Form.Label>
                            <Form.Control type="text" name="addr" value={formData.addr} onChange={handleEditAddressChange} readOnly />
                        </Form.Group>
                        <Form.Group controlId="formAddrDetail">
                            <Form.Label>상세주소</Form.Label>
                            <Form.Control type="text" name="addrDetail" value={formData.addrDetail} onChange={handleEditAddressChange} />
                        </Form.Group>
                        <Form.Group controlId="formDeliveryReq">
                            <Form.Label>배송요청사항</Form.Label>
                            <Form.Control type="text" name="deliveryReq" value={formData.deliveryReq} onChange={handleEditAddressChange} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleSaveEditedAddress}>수정하기</Button>
                </Modal.Footer>
            </Modal>

            <ToastContainer />
        </div>
    );
}

export default Order;
