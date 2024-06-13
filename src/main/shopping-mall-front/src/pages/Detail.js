import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Nav, Button, DropdownButton, Dropdown } from 'react-bootstrap/';
import useProductStore from '../stores/useProductStore.js';
import useUserStore from '../stores/useUserStore.js'; // import useUserStore
import './Detail.css';

/** 
 * Product에서 상품눌렀을때 로드되는 상세페이지
 */

function Detail() {
  const items = useProductStore(state => state.items);
  const { itemId } = useParams();
  const findId = items.find((x) => x.id == itemId);
  const navigate = useNavigate();
  const user = useUserStore(state => state.user);

  const [modal, setModal] = useState('detail');

  const handleBuyNow = () => {
    if (user) {
      navigate('/order', { state: { item: findId } });
    } else {
      window.alert("지금 당장 로그인하고 쌈@뽕하게 주문하세요!");
    }
  };

  const handleAddToCart = () => {
    if (!user) {
      window.alert("지금 당장 쌈@뽕하게 로그인하고 장바구니에 담으세요!");
      return;
    }

    // 현재 localStorage에 저장된 장바구니를 가져오기
    const cart = JSON.parse(localStorage.getItem('cart')) || [];

    // 현재 상품을 장바구니에 추가
    const updatedCart = [...cart, { ...findId, quantity: 1 }];

    // localStorage에 업데이트된 장바구니 저장
    localStorage.setItem('cart', JSON.stringify(updatedCart));

    window.alert("장바구니에 추가되었습니다.");
  };

  return (
    <div className="container detail-container">
      <div className="row">
        <div className="col-md-6 detail-image">
          <div className="image-placeholder"><img src={'https://codingapple1.github.io/shop/shoes' + findId.id + '.jpg'} width="100%" /></div>
        </div>
        <div className="col-md-6 detail-info">
          <h2>{findId.title}</h2>
          <p className="price">{findId.price}</p>
          {/* 나중에 DB에있는정보 끌어오는걸로 변경하는걸로.. */}
          <DropdownButton id="size-dropdown" title="사이즈">
            <Dropdown.Item>Small</Dropdown.Item>
            <Dropdown.Item>Medium</Dropdown.Item>
            <Dropdown.Item>Large</Dropdown.Item>
          </DropdownButton>
          <DropdownButton id="color-dropdown" title="색상">
            <Dropdown.Item>Red</Dropdown.Item>
            <Dropdown.Item>Blue</Dropdown.Item>
            <Dropdown.Item>Green</Dropdown.Item>
          </DropdownButton>
          <p className="total-price">총 상품 금액 <span>0</span></p>
          <div className="buttons">
            <Button variant="outline-dark" className="add-to-cart" onClick={handleAddToCart}>Add to cart</Button>
            <Button variant="outline-dark" className="buy-now" onClick={handleBuyNow}>Buy now</Button>
          </div>
        </div>
      </div>
      <Nav justify variant="tabs" defaultActiveKey="detail">
        <Nav.Item>
          <Nav.Link eventKey="detail" onClick={() => setModal('detail')}>상품 상세</Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link eventKey="review" onClick={() => setModal('review')}>리뷰</Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link eventKey="qna" onClick={() => setModal('qna')}>문의사항</Nav.Link>
        </Nav.Item>
      </Nav>
      {modal === 'detail' && <DetailModal />}
      {modal === 'review' && <ReviewModal />}
      {modal === 'qna' && <QnaModal />}
    </div>
  );

  function DetailModal() {
    return (
      <div className="DetailModal">
        <div>상세페이지 모달</div>
        <p>{findId.title}</p>
      </div>
    );
  }

  function ReviewModal() {
    return (
      <div className="ReviewModal">
        <div>리뷰페이지 모달</div>
      </div>
    );
  }

  function QnaModal() {
    return (
      <div className="QnaModal">
        <div>QnA 모달</div>
      </div>
    );
  }
}

export default Detail;
