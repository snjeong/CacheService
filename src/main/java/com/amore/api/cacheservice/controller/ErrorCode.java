package com.amore.api.cacheservice.controller;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : cacheservice
 * Developer : snjeong
 * Purpose :
 */
public enum ErrorCode {
    OK("0", "success", "1001", HttpStatus.OK),
    BAD_REQUEST("1", "bad request", "1002", HttpStatus.BAD_REQUEST),
    FORBIDDEN("2", "no permission", "1003", HttpStatus.UNAUTHORIZED),
    SERVICE_UNAVAILABLE("3", "service temporarily unavailable", "1004", HttpStatus.SERVICE_UNAVAILABLE),
    // 처리중 에러가 발생하였습니다.
    INTERNAL_SERVER_ERROR("4", "internal server error", "1005", HttpStatus.INTERNAL_SERVER_ERROR),
    // 서버에 오류가 발생하였습니다. 잠시 후 다시 거래하시기 바랍니다.
    CACHE_MISS("cache_miss", "unknown system error", "1006", HttpStatus.INTERNAL_SERVER_ERROR),

    // 입력값처리오류 (HttpStatus.BAD_REQUEST)
    PARAM_ERROR_REQUIRED("6", "check required parameters", "1007", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(String apiResponseCode, String apiResonseMessage, String errCode, HttpStatus httpStatus) {
        this.apiResponseCode = apiResponseCode;
        this.apiResonseMessage = apiResonseMessage;
        this.errCode = errCode;
        this.httpStatus = httpStatus;
    }

    private static Map<String, ErrorCode> errorCodeMap;

    private String apiResponseCode;
    private String apiResonseMessage;
    private String errCode;
    private HttpStatus httpStatus;

    private static void initializeMapping() {
        errorCodeMap = new HashMap<String, ErrorCode>();
        for (ErrorCode errorCode : ErrorCode.values()) {
            errorCodeMap.put(errorCode.apiResponseCode, errorCode);
        }
    }

    public static final ErrorCode getErrorCodeByApiResponse(String value) {

        if (errorCodeMap == null)
            initializeMapping();

        if (errorCodeMap.containsKey(value))
            return errorCodeMap.get(value);
        else
            return null;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getApiResponseCode() {
        return apiResponseCode;
    }

    public String getApiResonseMessage() {
        return apiResonseMessage;
    }

    public void setApiResponseCode(String apiResponseCode) {
        this.apiResponseCode = apiResponseCode;
    }

    public void setApiResonseMessage(String apiResonseMessage) {
        this.apiResonseMessage = apiResonseMessage;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}

