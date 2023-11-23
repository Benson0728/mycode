package com.grade.controller;

import com.grade.context.Message;
import com.grade.pojo.StuCourseTeacher;
import com.grade.pojo.StuGrades;
import com.grade.pojo.Student;
import com.grade.result.LoginRes;
import com.grade.result.Res;
import com.grade.service.StudentService;
import com.grade.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping ("/login")   //学生登录
    public LoginRes studentLogin(long id,String password){
        Student student = studentService.selectById(id);
        boolean check=password.equals(student.getPassword());
        if (check==true)return new LoginRes<>().success(JwtUtils.createToken(student.getId(),student.getName()));
        return new LoginRes<>().fail();
    }

    @GetMapping  //查询某个学生信息
    public Res checkInfo(){
        //从threadlocal中拿数据
        Claims claims = JwtUtils.getClaims();
         long id= (Long) claims.get("ID");
        Student student = studentService.selectById(id);
        return new Res<>().success(student);
    }

    @PutMapping  //修改密码
    public Res updatePassword(String password){
        Claims claims = JwtUtils.getClaims();
        long id= (Long) claims.get("ID");
        Integer integer = studentService.updatePassword(id, password);
        return integer==1?new Res<>().success():new Res<>().fail(Message.PASSWORD_EDIT_FAILED);
    }

    @GetMapping("/grades") //查询成绩
    public Res checkGrades(){
        Claims claims = JwtUtils.getClaims();
        long id= (Long) claims.get("ID");
        List<StuGrades> grades = studentService.checkGrades((long) id);
        return new Res<>().success(grades);
    }

    @GetMapping("/schedule") //查询课表
    public Res checkSchedule(){
        Claims claims = JwtUtils.getClaims();
        long id= (Long) claims.get("ID");
        List<StuCourseTeacher> schedules = studentService.checkSchedule(id);
        return new Res<>().success(schedules);
    }

    @PutMapping("/chooseCourses/{course}") //学生选课
    public Res chooseCourses(@PathVariable String course){
        Claims claims = JwtUtils.getClaims();
        long id=Long.valueOf (claims.get("ID").toString());
        String stuName=(String) claims.get("username");
        boolean isSuccess = studentService.chooseCourses(course, id, stuName);
        return isSuccess==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }
}
