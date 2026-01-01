package com.vetsync.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "입력하신 정보가 올바르지 않습니다. 다시 확인해 주세요."),
    ENTITY_ALREADY_EXISTS(HttpStatus.CONFLICT, "C002", "이미 등록된 정보입니다. 다른 정보로 시도해 주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "요청하신 페이지나 기능을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "일시적인 서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "입력 형식이 올바르지 않습니다. 올바른 형식으로 입력해 주세요."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C006", "해당 작업에 대한 권한이 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C007", "지원하지 않는 요청 방식입니다. 올바른 방식으로 다시 시도해 주세요."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "C008", "필수 파라미터가 누락되었습니다. 요청을 확인해 주세요."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "C009", "JSON 형식이 올바르지 않습니다. 요청 데이터를 확인해 주세요."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "C010", "지원하지 않는 미디어 타입입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "C011", "파일 크기가 허용된 범위를 초과했습니다."),
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "C012", "요청 시간이 초과되었습니다. 다시 시도해 주세요."),
    HOSPITAL_NOT_FOUND(HttpStatus.NOT_FOUND, "C013", "요청하신 병원을 찾을 수 없습니다."), // 추가
    INVALID_REFERENCE(HttpStatus.BAD_REQUEST, "C014", "요청한 참조값이 유효하지 않습니다."),

    // Auth
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A003", "로그인이 만료되었습니다. 다시 로그인해 주세요."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A004", "인증 정보가 올바르지 않습니다. 다시 로그인해 주세요."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A005", "로그인 세션이 만료되었습니다. 다시 로그인해 주세요."),
    SMS_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "A006", "인증번호가 올바르지 않거나 만료되었습니다. 다시 확인해 주세요."),

    // Owner
    OWNER_ALREADY_EXISTS(HttpStatus.CONFLICT, "O001", "이미 등록된 보호자입니다."),

    // Patient
    PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "요청하신 환자를 찾을 수 없습니다."),

    // Task
    TASK_DEFINITION_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "요청하신 업무 정의를 찾을 수 없습니다."),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "T002", "요청하신 업무를 찾을 수 없습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;
}
