package com.itsconv.http;

/**
 * 콜백 등록 API 호출 결과를 보관한다.
 */
public final class CallbackApiResult {

    private final boolean success;
    private final String callbackId;
    private final String failureReason;

    private CallbackApiResult(
            boolean success,
            String callbackId,
            String failureReason
    ) {
        this.success = success;
        this.callbackId = valueOrEmpty(callbackId);
        this.failureReason = valueOrEmpty(failureReason);
    }

    public static CallbackApiResult success(String callbackId) {
        return new CallbackApiResult(true, callbackId, "");
    }

    public static CallbackApiResult failure(String failureReason) {
        return new CallbackApiResult(false, "", failureReason);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
