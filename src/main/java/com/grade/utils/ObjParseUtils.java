package com.grade.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public  class ObjParseUtils{
        public static <T> T ObjParse(Object obj,T t){
            String s = JSON.toJSONString(obj);
            return (T) JSONObject.parseObject(s, t.getClass());
        }
    }

