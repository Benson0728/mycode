package com.grade;

import com.grade.pojo.StuGrades;
import com.grade.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GradeManagementSystemApplicationTests {
@Autowired
    StudentService studentService;
    @Test
    void contextLoads() {
        List<StuGrades> list = studentService.checkGrades(2022091292006L);
    }

}
