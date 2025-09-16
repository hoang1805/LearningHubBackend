package com.example.learninghubbackend.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int code;

    private BaseResponse(boolean success, String message, T data, int code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, "", data, 0);
    }

    public static BaseResponse<Void> success() {
        return new BaseResponse<>(true, "", null, 0);
    }

    public static <T> BaseResponse<T> error(String message, T data, int code) {
        return new BaseResponse<>(false, message, data, code);
    }

    public static BaseResponse<Void> error(String message, int code) {
        return new BaseResponse<>(false, message, null, code);
    }
}
