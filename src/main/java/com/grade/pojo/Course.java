package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("course")
public class Course implements Serializable {
    @TableId(value = "course_id",type = IdType.AUTO)
    private Integer id;
    private String courseName;
    private float commonGradePercent;
    private float finalGradePercent;
    private String teacherName;
    private float aveGrades;
}
