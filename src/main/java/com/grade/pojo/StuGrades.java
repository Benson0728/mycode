package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@AllArgsConstructor
@Data
@TableName("student_grades")
public class StuGrades implements Serializable {
    private long studentId;
    private String studentName;
    @TableField("class")
    private Integer clazz;
    private String courseName;
    private Integer commonGrades;
    private Integer finalGrades;
    private float grades;
}
