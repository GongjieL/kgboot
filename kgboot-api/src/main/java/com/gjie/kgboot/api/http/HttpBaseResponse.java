package com.gjie.kgboot.api.http;

public class HttpBaseResponse<T> {
    /**
     * 返回code
     */
    private Integer code;

    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 失败信息
     */
    private String message;

    /**
     * 返回
     */
    private T data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
