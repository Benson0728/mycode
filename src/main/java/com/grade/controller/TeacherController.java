package com.grade.controller;

import com.grade.context.Message;
import com.grade.pojo.*;
import com.grade.result.LoginRes;
import com.grade.result.Res;
import com.grade.service.TeacherService;
import com.grade.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.util.ElementKindVisitor14;
import java.nio.channels.Pipe;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    @PostMapping ("/login")   //教师登录
    public LoginRes studentLogin(Integer id, String password){
        Teacher teacher = teacherService.selectById(id);
        boolean check=password.equals(teacher.getPassword());
        if (check==true)return new LoginRes<>().success(JwtUtils.createToken(teacher.getId(),teacher.getName()));
        return new LoginRes<>().fail();
    }

    @PutMapping("/{id}")  //修改密码
    public Res updatePassword(@PathVariable Integer id,String password){
        Integer integer = teacherService.updatePassword(id, password);
        return integer==1?new Res<>().success():new Res<>().fail(Message.PASSWORD_EDIT_FAILED);
    }

    @GetMapping("/grades/{teacherName}") //查询自己所教科目成绩
    public Res checkGrades(@PathVariable String teacherName){
        List<Course> list= teacherService.selectGrades(teacherName);
        return new Res<>().success(list);
    }

    @GetMapping("/grades/student/id/{course}/{stuId}") //根据学号查成绩
    public Res checkGradesById(@PathVariable("course") String course,@PathVariable("stuId") long stuId){
        StuGrades grades = teacherService.selectGrades(stuId, course);
        return grades!=null?new Res<>().success(grades):new Res<>().fail(Message.STU_NOT_EXIST);
    }

    @GetMapping("/grades/student/name/{course}/{name}") //根据名字查成绩
    public Res checkGradesByName(@PathVariable("course") String course,@PathVariable("name") String name,int current,int size){
        PageResult page = teacherService.selectGrades(name, course,current,size);
        return page.getList()!=null?new Res<>().success(page):new Res<>().fail(Message.STU_NOT_EXIST);
    }

    @GetMapping("/grades/class/{course}/{classNum}") //查询班级成绩
    public Res checkGrades(@PathVariable("course") String course,@PathVariable("classNum") Integer classNum){
        ClassGrades classGrades = teacherService.selectClassGrades(classNum, course);
        return new Res<>().success(classGrades);
    }

    @GetMapping("/grades/sort/{course}") //班级成绩排名
    public Res checkClassGrades(@PathVariable String course,int current,int size){
        PageResult page= teacherService.selectClassGrades(course,current,size);
        return new Res<>().success(page);
    }

    @GetMapping("/grades/sort/{course}/{classNum}") //班内学生排名
    public Res checkStuGradesRank(@PathVariable("course") String course,@PathVariable("classNum") Integer classNum,int current,int size){
        PageResult page= teacherService.classStuRank(classNum, course,current,size);
        return new Res<>().success(page);
    }

    @GetMapping("/grades/sortAll/{course}") //所有学生排名
    public Res sortAllStu(@PathVariable String course,int current,int size){
        PageResult page = teacherService.allStuRank(course,current,size);
        return new Res<>().success(page);
    }

    @GetMapping("schedule/{teacherName}") //查看课表
    public Res checkSchedule(@PathVariable String teacherName){
        List<StuCourseTeacher> schedule = teacherService.checkSchedule(teacherName);
        return new Res<>().success(schedule);
    }
}