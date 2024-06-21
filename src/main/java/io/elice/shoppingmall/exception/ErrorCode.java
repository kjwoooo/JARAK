package io.elice.shoppingmall.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND_MEMBER(400, "회원 정보를 찾을 수 없습니다."),
    NOT_FOUND_PROVIDER_ID(400, "해당 기간의 정보로 가입된 회원을 찾을 수 없습니다."),
    NOT_MATCH_EMAIL(400, "이메일 형식이 잘못되었습니다."),
    NOT_FOUND_ADDRESS(400, "주소 정보를 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY(400, "존재하지 않는 카테고리입니다."),
    NOT_FOUND_PARENT_CATEGORY(400, "존재하지 않는 상위 카테고리입니다."),
    NOT_FOUND_ITEM(400, "존재하지 않는 아이템 입니다."),
    NOT_FOUND_ITEM_DETAIL(400, "상품 세부 정보를 찾을 수 없습니다."),
    NOT_FOUND_BRAND(400, "존재하지 않는 브랜드입니다."),
    NOT_FOUND_REVIEW(400, "존재하지 않는 리뷰입니다."),
    NOT_FOUND_ORDER(400, "주문을 찾을 수 없습니다."),
    NOT_FOUND_ITEM_IMAGE(400, "존재하지 않는 사진입니다."),

    MEMBER_PASSWROD_WRONG(400, "비밀번호가 잘못되었습니다."),

    EXIST_USERNAME(400, "이미 존재하는 아이디 입니다."),
    EXIST_EMAIL(400, "이미 존재하는 이메일 입니다."),
    EXIST_BRAND_NAME(400, "이미 존재하는 브랜드입니다."),
    EXIST_CATEGORY_NAME(400, "이미 존재하는 카테고리입니다."),

    NOT_MATCHE_ADDRESS_TO_MEMBER(400, "회원 정보와 주소 정보가 일치하지 않습니다."),

    DO_NOT_HAVE_AUTHORITY(403, "접근 권한이 없습니다."),

    CANNOT_MODIFY_CANCELLED_ORDER(400, "취소된 주문은 수정할 수 없습니다."),
    CANNOT_MODIFY_ORDER_STATE(400, "상품이 발송되었거나 배송 완료된 주문은 배송지 변경이 불가합니다."),
    INVALID_ORDER_STATE(400, "유효하지 않은 주문 상태입니다."),
    ORDER_COUNT_ERROR(500, "주문 수를 조회하는 중 오류가 발생했습니다."),
    INVALID_PAGING_PARAMETERS(400, "페이지 매개변수가 유효하지 않습니다."),
    NO_ORDER_DETAILS_FOUND(400, "주문 세부 정보를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(400, "해당 상품을 찾을 수 없습니다."),

    //상품 등록관련 error codes
    INVALID_IMAGE_FILE(500, "이미지 파일이 아닙니다."),

    // 유효성 검사(Validation) error codes
    INVALID_ORDER_DETAILS(400, "주문 세부 정보가 유효하지 않습니다."),

    NULL_POINT_MEMBER(500, "회원 정보가 없습니다.");



    private final int state;
    private final String message;

    private ErrorCode(int state, String message){
        this.state = state;
        this.message = message;
    }
}
