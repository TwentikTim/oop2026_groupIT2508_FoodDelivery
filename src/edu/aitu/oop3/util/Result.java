package edu.aitu.oop3.util;

public class Result<T> {

    private final boolean success;
    private final T data;
    private final String error;

    public Result(boolean success, T data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null);
    }

    public static <T> Result<T> failure(String error){
        return new Result<>(false, null, error);
    }

    public boolean isSuccess() {
        return success;
    }


    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
