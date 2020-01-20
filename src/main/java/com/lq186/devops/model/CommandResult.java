package com.lq186.devops.model;

/**
 * @author lq
 * @date 2020/1/19
 */
public final class CommandResult<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 失败信息
     */
    private String message;

    /**
     * 成功数据
     */
    private T data;

    public CommandResult() {
    }

    public CommandResult(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> CommandResult<T> failed(String message) {
        return new CommandResult<>(false, message, null);
    }

    public static <T> CommandResult<T> success(T data) {
        return new CommandResult<>(true, null, data);
    }

    public static CommandResult<Void> success() {
        return new CommandResult<>(true, null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public CommandResult<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public CommandResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommandResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
