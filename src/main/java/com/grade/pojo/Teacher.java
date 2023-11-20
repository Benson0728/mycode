package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("teacher")
public class Teacher implements Serializable {
@TableId(value = "teacher_id")
    private long id;
    private String name;
    private String password;
    private String school;
}
