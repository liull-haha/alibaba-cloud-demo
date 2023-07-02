package com.demo.example.common.base;

/**
 * @author liull
 * @description
 * @date 2021/9/1
 */
public enum BaseResult implements IBaseResult{
    /**
     * 成功
     */
    SUCCESS("0000","成功"),
    /**
     * 系统异常
     */
    FAILURE("9999","系统异常")
    ;
    private String code;
    private String massge;

    BaseResult(String code, String massge) {
        this.code = code;
        this.massge = massge;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMassge() {
        return massge;
    }
}
