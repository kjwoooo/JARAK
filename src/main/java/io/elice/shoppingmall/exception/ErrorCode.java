package io.elice.shoppingmall.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND_MEMBER(400, "회원 정보를 찾을 수 없습니다."),
    NOT_MATCH_EMAIL(400, "이메일 형식이 잘못되었습니다."),
    NOT_FOUND_ADDRESS(400, "주소 정보를 찾을 수 없습니다."),

    MEMBER_PASSWROD_WRONG(400, "비밀번호가 잘못되었습니다."),

    EXIST_USERNAME(400, "이미 존재하는 아이디 입니다."),
    EXIST_EMAIL(400, "이미 존재하는 이메일 입니다.");


    private final int state;
    private final String message;

    private ErrorCode(int state, String message){
        this.state = state;
        this.message = message;
    }
}
