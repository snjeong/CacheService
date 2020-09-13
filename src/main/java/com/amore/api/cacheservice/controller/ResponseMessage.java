package com.amore.api.cacheservice.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;

/**
 * Project : cacheservice
 * Developer : snjeong
 * Purpose : Rest API 결과 반환을 위한 사용자 정의 오류코드와 메세지
 */
public class ResponseMessage {
    public static ResponseMessage ok() {
        return new ResponseMessage(ErrorCode.OK);
    }

    public static ResponseMessage create(ErrorCode errorCode) {
        return new ResponseMessage(errorCode);
    }

    public ResponseMessage(ErrorCode errorCode) {
        this.errorCode = errorCode.getApiResponseCode();
        this.errorMsg = errorCode.getApiResonseMessage();
    }

    @JsonProperty("error_code")
    String errorCode;

    @JsonProperty("error_msg")
    String errorMsg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("result")
    Object result;

    public ResponseMessage add(String k, String v) {

        this.result = new LinkedHashMap<String, String>();

        ((LinkedHashMap<String, String>)this.result).put(k, v);

        return this;
    }

    public ResponseMessage set(Object v) {
        this.result = v;

        return this;
    }
}