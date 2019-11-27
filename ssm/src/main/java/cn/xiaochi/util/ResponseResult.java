package cn.xiaochi.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回 json 格式数据
 */
public class ResponseResult {
    //状态码
    private int code;
    //消息
    private String msg;
    //返回数据
    private Object data;
    // 成功值
    private static final int SUCCESS_CODE = 1;
    // 失败值
    private static final int ERROR_CODE = 0;

    public ResponseResult () {}

    public ResponseResult (int code,String msg,Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功
    public static ResponseResult success(){
        return new ResponseResult(SUCCESS_CODE,"success",null);
    }

    public static ResponseResult success(String msg){
        return new ResponseResult(SUCCESS_CODE,msg,null);
    }

    public static ResponseResult success(Object data){
        return new ResponseResult(SUCCESS_CODE,"success",data);
    }

    public static ResponseResult success(String msg,Object data){
        return new ResponseResult(SUCCESS_CODE,msg,data);
    }
    // 失败
    public static ResponseResult error(){
        return new ResponseResult(ERROR_CODE,"fail",null);
    }

    public static ResponseResult error(String msg){
        return new ResponseResult(ERROR_CODE,msg,null);
    }

    public static ResponseResult error(Object data){
        return new ResponseResult(ERROR_CODE,"fail",data);
    }

    public Map<String,Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",this.code);
        map.put("msg",this.msg);
        map.put("data",this.data);
        return map;
    };

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    public static int getErrorCode() {
        return ERROR_CODE;
    }
}
