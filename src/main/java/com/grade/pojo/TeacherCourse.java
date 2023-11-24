package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("teacher_course")
@Data
public class TeacherCourse {
    private String teacherName;
    private Integer day;
    private Integer time;
    private String courseName;

}
