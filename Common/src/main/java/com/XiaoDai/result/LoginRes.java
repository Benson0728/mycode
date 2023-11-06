package com.XiaoDai.result;

import com.XiaoDai.constant.Message;


import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRes<T> implements Serializable {
    private int code;
    private String message;
    private T data;
    private String token;

    public static LoginRes success(String token){
        LoginRes loginRes= new LoginRes();
        loginRes.token=token;
        loginRes.code=1;
        return loginRes;
    }

    public static LoginRes fail(){
        LoginRes loginRes=new LoginRes();
        loginRes.code=0;
        loginRes.message= Message.PASSWORD_ERR;
        return loginRes;
    }
}
