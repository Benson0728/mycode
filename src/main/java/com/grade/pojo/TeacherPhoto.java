package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher_photo")
public class TeacherPhoto {
    @TableField("teachername")
    private String teacherName;
    private String photoPath;

    public TeacherPhoto() {
    }

    public TeacherPhoto(String teacherName, String photoPath) {
        this.teacherName = teacherName;
        this.photoPath = photoPath;
    }
}
