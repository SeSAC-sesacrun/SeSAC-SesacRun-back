package com.example.sesacrunback.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    //Token
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰 서명이 유효하지 않습니다."),
    MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "토큰 형식이 올바르지 않습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원하지 않는 토큰 형식입니다."),
    EMPTY_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),

    //Auth
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),

    // 모집 글
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    POST_NOT_OWNER(HttpStatus.FORBIDDEN, "해당 게시글의 작성자가 아닙니다."),
    POST_INVALID_TOTAL_MEMBERS(HttpStatus.BAD_REQUEST, "모집 인원은 현재 참여 인원보다 적게 설정할 수 없습니다."),
    RECRUITMENT_FULL(HttpStatus.CONFLICT, "모집 인원이 모두 찼습니다."),

    // 모임
    ALREADY_RECRUITED_MEMBER(HttpStatus.CONFLICT, "이미 해당 모임에 참여 중입니다."),
    ALREADY_APPLIED(HttpStatus.CONFLICT, "이미 신청된 모임입니다."),
    NOT_RECRUITMENT_MEMBER(HttpStatus.FORBIDDEN, "해당 모임의 멤버가 아닙니다."),
    NOT_RECRUITMENT_ORGANIZER(HttpStatus.FORBIDDEN, "요청을 처리할 권한이 없습니다."),
    RECRUITED_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "모임 참여 신청을 찾을 수 없습니다."),
    MEMBER_STATUS_NOT_PENDING(HttpStatus.CONFLICT, "대기 상태가 아닙니다."),

    // 채팅
    CANNOT_CHAT_WITH_SELF(HttpStatus.BAD_REQUEST, "본인에게는 채팅을 시작할 수 없습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방이 존재하지 않습니다."),
    CHAT_NOT_PARTICIPANT(HttpStatus.FORBIDDEN, "채팅 참여자가 아닙니다."),

    // Common
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // User
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),

    // Course
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "코스를 찾을 수 없습니다."),

    // Section
    SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "섹션을 찾을 수 없습니다."),
    SECTION_ORDER_DUPLICATE(HttpStatus.CONFLICT, "해당 순서에 이미 다른 섹션이 존재합니다."),

    // Lecture
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다."),
    LECTURE_ORDER_DUPLICATE(HttpStatus.CONFLICT, "해당 순서에 이미 다른 강의가 존재합니다."),

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
    PAYMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 결제한 강의입니다."),
    //REFUND
    ORDER_ALREADY_CANCELED(HttpStatus.CONFLICT, "이미 취소된 주문입니다."),
    REFUND_ALREADY_PROCESSED(HttpStatus.CONFLICT, "이미 처리된 환불 요청입니다."),
    REFUND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "환불 처리에 실패했습니다. 잠시 후 다시 시도해 주세요.");


    private final HttpStatus status;
    private final String message;
}
