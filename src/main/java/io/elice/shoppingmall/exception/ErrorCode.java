package io.elice.shoppingmall.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND_MEMBER(400, "회원 정보를 찾을 수 없습니다."),
    NOT_FOUND_PROVIDER_ID(400, "해당 기간의 정보로 가입된 회원을 찾을 수 없습니다."),
    NOT_MATCH_EMAIL(400, "이메일 형식이 잘못되었습니다."),
    NOT_FOUND_ADDRESS(400, "주소 정보를 찾을 수 없습니다."),

    MEMBER_PASSWROD_WRONG(400, "비밀번호가 잘못되었습니다."),

    EXIST_USERNAME(400, "이미 존재하는 아이디 입니다."),
    EXIST_EMAIL(400, "이미 존재하는 이메일 입니다."),

    DO_NOT_HAVE_AUTHORITY(403, "접근 권한이 없습니다."),

    NOT_FOUND_CATEGORY(400, "존재하지 않는 카테고리입니다."),
    NOT_FOUND_PARENT_CATEGORY(400, "존재하지 않는 상위 카테고리입니다."),
    EXIST_CATEGORY_NAME(400, "이미 존재하는 카테고리입니다."),

    NOT_FOUND_BRAND(400, "존재하지 않는 브랜드입니다."),
    EXIST_BRAND_NAME(400, "이미 존재하는 브랜드입니다.");

    private final int state;
    private final String message;

    private ErrorCode(int state, String message){
        this.state = state;
        this.message = message;
    }
}
