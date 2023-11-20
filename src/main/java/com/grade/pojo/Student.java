package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@Data
@TableName("student")
public class Student implements Serializable {
    @TableId("student_id")
    private long id;
    private String name;
    private String password;
    private String sex;
    private String school;
    @TableField("class")
    private Integer clazz;
}
