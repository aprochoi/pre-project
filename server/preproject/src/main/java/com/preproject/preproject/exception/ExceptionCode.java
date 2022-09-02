package com.preproject.preproject.exception;

import lombok.Getter;

public enum ExceptionCode {
    ANSWER_NOT_FOUND(404, "답변을 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(404, "이메일을 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(400,"비밀번호가 틀렸습니다.");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}