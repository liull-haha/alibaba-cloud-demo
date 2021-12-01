package com.demo.example.common.base;

/**
 * @author liull
 * @description  统一返回数据封装对象
 * @date 2021/9/1
 */
public class BaseResponse <T>{
    /**
     * 返回码
     */
    private String code;

    /**
     * 返回描述
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    public BaseResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 自定义返回
     * @param code
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> createResp(String code, String message, T data){
        return new BaseResponse<T>(code,message,data);
    }

    /**
     * 基础成功返回，没有封装数据
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(){
        return new BaseResponse<T>(BaseResult.SUCCESS.getCode(),BaseResult.SUCCESS.getMassge());
    }

    /**
     * 基础成功返回，且有封装数据
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(BaseResult.SUCCESS.getCode(),BaseResult.SUCCESS.getMassge(),data);
    }

    /**
     * 基础的系统异常返回
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> failure(){
        return new BaseResponse<>(BaseResult.FAILURE.getCode(), BaseResult.FAILURE.getMassge());
    }

    /**
     * 基础的系统异常返回，自定义返回描述
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> failure(String message){
        return new BaseResponse<>(BaseResult.FAILURE.getCode(), message);
    }

    /**
     * 自定义失败返回
     * @param iBaseResult
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> failure(IBaseResult iBaseResult){
        return new BaseResponse<>(iBaseResult.getCode(),iBaseResult.getMassge());
    }

}
