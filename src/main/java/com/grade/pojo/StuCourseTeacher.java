package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("student_course_teacher")
public class StuCourseTeacher implements Serializable {
    private long studentId;
    private String studentName;
    private String courseName;
    private Integer day;
    private Integer time;
    private String teacherName;
}
