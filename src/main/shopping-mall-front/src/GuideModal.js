import React from 'react';
import './GuideModal.css';

function GuideModal() {
  return (
    <div className="Detail_GuideModal">
      <div className="AllGuide">
        <div>배송 / 교환 / 반품 안내</div>
        <p>고객센터 상담시간</p>
        <p>AM 01:00 ~ AM 02:00</p>
        <p>토요일, 일요일, 공휴일, 임시공휴일, 평일은 상담 제외</p>
        <p>반송지 주소</p>
        <p>서울특별시 성동구 아차산로17길 48 성수낙낙 C동 2층 엘리스Lab 성수</p>
      </div>

      <div className="DeliveryGuide">
        <div>배송안내</div>
        <p>배송일정: 입금완료 후 평균 3~7일 소요</p>
        <p>입금 및 환불 문의는 [(주)엘리스070-4633-2017] 로 해주시기 바랍니다.</p>
        <p>배송전 주문옵션변경, 배송전 취소는 반드시 당사와 연락 후 처리하셔야 합니다.</p>
        <p>이미 출고된 상품은 수취거절 불가합니다.(게시판 혹은 메일 요청시 그에 대한 오배송이나 출고건은 책임지지 않습니다.)</p>
        <p>제주 및 도서 산간지역은 택배사 배송비 규정 정책으로 고객님의 부담으로 추가 운임비용이 발생할 수 있습니다.</p>
      </div>

      <div className="ExchangeAndReturnGuide">
        <div>교환/반품 안내</div>
        <p>상품 수령 후 단순변심의 경우 7일 이내에 당사에 교환/반품접수 하셔야 합니다.</p>
        <p>교환/반품시에는 왕복 택배비 6,000원을 상품과 동봉하여 우편배달부 키키를 이용해서 보내주시면 됩니다.
          <br/>(단, 쇼핑몰에서 반품비를 선결제 또는 차감 하신경우 동봉하지 않으셔도 됩니다.)
        </p>
        <p>반품시 기재내용 : 수취인 성명, 연락처, 반품&교환 사유 등 메모 미동봉시 처리가 지연될 수 있습니다.</p>
        <p>교환은 상품명, 색상, 사이즈등 자세히 기재하셔야 합니다. (동일상품, 동일가격에 한함)</p>
        <p>불량으로 인한 교환반품은 불량부분을 자세히 기재하여 메모 동봉 하셔야 합니다.</p>
        <p>고객님의 부주의로 인한 손상이나 훼손된 제품은 반품 및 교환이 불가합니다.</p>
      </div>
      
      <div className="etcGuide">
        <div>기타안내</div>
        <p>고객님께서 상품은 포장 개봉하여 착용 후 상품의 가치가 훼손된 경우 교환/반품 신청이 제한됩니다.</p>
        <p>모니터 사양에 따라 실제 색상과 차이가 있을 수 있습니다.</p>
        <p>판매 상품을 제외한 아이템은 코디용 제품으로 상품구성에 포함되어 있지 않습니다.</p>
        <p>고객님의 단순변심 및 색상/사이즈 불만에 의한 교환/반품 비용은 고객님께서 부담하셔야 합니다.</p>
        <p>(단 상품의 불량/하자를 포함한 표시광고 및 계약내용과 다른 경우 해당 상품 회수/배송 비용은 무료입니다)</p>
        <p>무료배송상품, 묶음배송 받으신 상품일 경우 한가지라도 반송시 6,000원(초기 배송비 포함)을 고객님께서 부담해 주셔야 합니다.</p>
        <p>개별 결제하여 배송비가 구매 상품마다 부과되었을 경우 업체에서 배송비 환불이 이루어지지 않으므로 <br/>장바구니에 담은 후 한꺼번에 결제해 주셔야 합니다.</p>
        <p>사은품을 받으신 경우 본 상품과 함께 반송해 주셔야 하며, 사은품 불량 등으로 무상 교환반품은 어려울 수 있는 점 양해 부탁드립니다.</p>
        <p>상품이 출고 된 후 주문 취소시 왕복배송비 6,000원은 고객님 부담입니다.</p>
        <p>사이즈는 측정 방법과 리오더 과정에 따라 약간의 오차가 생길 수 있습니다.</p>
        <p>사이즈 스펙은 상품의 실측사이즈이며, 상품라벨에 표기된 사이즈는 신체표준사이즈로 실측사이즈와는 다를 수 있습니다. <br/>이로 인한 무상반품, 교환은 어려운점 양지하시고 주문해주세요</p>
        <p>제작 과정의 특성상, 생산시마다 색상/원단/사이즈/부자재등이 약간 달라 질 수 있습니다.</p>
      </div>
    </div>
  );
}

export default GuideModal;
