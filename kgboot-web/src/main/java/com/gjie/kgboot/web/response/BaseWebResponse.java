package com.gjie.kgboot.web.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseWebResponse<T> {
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

}
