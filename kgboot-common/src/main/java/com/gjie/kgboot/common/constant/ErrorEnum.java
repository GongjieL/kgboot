package com.gjie.kgboot.common.constant;

public enum ErrorEnum {
    UN_KNOWN_ERROR(0, "未知错误"),
    PARAM_ERROR(1, "参数错误"),
    NO_EXISTS_HTTP_RESP_PROCESSOR(2, "找不到http返回处理类"),
    METHOD_EXECUTE_ERROR(3, "方法执行失败"),
    MSG_SEND_ERROR(4, "发送消息失败"),

    ;

    private Integer code;
    private String desc;


    ErrorEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
