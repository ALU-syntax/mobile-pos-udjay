package com.udjaya.kasirudjay.model;

public class LogRequest {
    private String timestamp;
    private String error_message;
    private String stack_trace;
    private DeviceInfo device_info;
    private UserInfo user_info;

    public LogRequest(String string, String message, String stackTraceString, DeviceInfo deviceInfo, UserInfo userInfo) {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getStack_trace() {
        return stack_trace;
    }

    public void setStack_trace(String stack_trace) {
        this.stack_trace = stack_trace;
    }

    public DeviceInfo getDevice_info() {
        return device_info;
    }

    public void setDevice_info(DeviceInfo device_info) {
        this.device_info = device_info;
    }

    public UserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
    }
}
