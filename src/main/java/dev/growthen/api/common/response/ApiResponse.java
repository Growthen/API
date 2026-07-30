package dev.growthen.api.common.response;

public record ApiResponse <T>(
        Boolean success,
        String message,
        T data
){
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
}
