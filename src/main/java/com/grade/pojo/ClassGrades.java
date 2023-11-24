package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("class_grades")
public class ClassGrades implements Serializable {
    @TableField("class")
    private int clazz;
    private String courseName;
    private float aveGrades;
}
