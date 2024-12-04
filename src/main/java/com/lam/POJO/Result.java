package com.lam.POJO;

import lombok.Data;

@Data
public class Result {
    private int code ;
    private String msg;
    private Object data;
    public Result(int code ,String msg,Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result error(Object data){
        return new Result(500,"失败",data);
    }

    public static Result success(Object data){
        return new Result(200,"成功",data);
    }

}
