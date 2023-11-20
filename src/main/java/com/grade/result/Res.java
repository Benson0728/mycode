package com.grade.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Res<T> implements Serializable {
    private int code;
    private String message;
    private T data;


    public static <T> Res<T> success(){
        Res<T> res= new Res<T>();
        res.code=1;
        return res;
    }
    public static <T> Res<T> success(T data){
        Res<T> res= new Res<T>();
        res.code=1;
        res.data=data;
        return res;
    }
    public Res fail(String message){
        Res<T> res= new Res<>();
        res.code=0;
        res.message=message;
        return res;
    }

}
