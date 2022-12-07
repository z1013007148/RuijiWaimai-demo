package com.example.waimai.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用结果返回类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;
    private Map map = new HashMap<>();

    public static <T> Result<T> success(T object, String msg){
        Result<T> r = new Result<T>();
        r.data = object;
        r.code = 1;
        r.msg = msg;
        return r;
    }

    public static <T> Result<T> error(String msg){
        Result<T> r = new Result<T>();
        r.data = null;
        r.code = 0;
        r.msg = msg;
        return r;
    }

    public Result<T> add(String key, Object value){
        this.map.put(key, value);
        return this;
    }
}
