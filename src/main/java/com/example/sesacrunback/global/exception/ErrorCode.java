package com.example.sesacrunback.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    //Auth
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),

    // 모집 글
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    POST_NOT_OWNER(HttpStatus.FORBIDDEN, "해당 게시글의 작성자가 아닙니다."),
    POST_INVALID_TOTAL_MEMBERS(HttpStatus.BAD_REQUEST, "모집 인원은 현재 참여 인원보다 적게 설정할 수 없습니다."),

    // Common
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // User
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),

    // Course
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),

    // Cart
    CART_ITEM_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 장바구니에 담긴 강의입니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에서 해당 항목을 찾을 수 없습니다."),
    // ORDER
    ORDER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 구매한 강의입니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문내역이 없습니다."),
    // PAYMENT
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "주문 금액과 실결제 금액이 일치하지 않습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제내역이 없습니다."),
    JSON_SERIALIZATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "데이터를 JSON으로 변환하는 데 실패했습니다."),
    PAYMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 결제한 강의입니다.");


    private final HttpStatus status;
    private final String message;
}
