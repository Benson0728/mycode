package com.XiaoDai.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@TableName("users")
public class User implements Serializable {
    @TableId(value = "user_id",type = IdType.AUTO)
    private long userId;
    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;
    private Date registrationDate;
}
