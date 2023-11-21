package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
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
