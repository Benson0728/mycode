package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("admin")
public class Admin implements Serializable {
    @TableId("admin_id")
    private long id;
    private String adminName;
    private String password;
}
