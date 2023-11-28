package com.grade.controller;

import com.grade.context.Message;
import com.grade.pojo.*;
import com.grade.result.LoginRes;
import com.grade.result.Res;
import com.grade.service.AdminService;
import com.grade.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/login")   //管理员登录
    public LoginRes adminLogin(Integer id, String password){
        Admin admin = adminService.selectById(id);
        log.info("{}登陆了",id);
        boolean check=password.equals(admin.getPassword());
        if (check==true)return new LoginRes<>().success(JwtUtils.createToken(admin.getId(),admin.getAdminName()));
        return new LoginRes<>().fail();
    }

    @PutMapping("/{id}")  //修改密码
    public Res updatePassword(@PathVariable long id, String password){
        Integer integer = adminService.updatePassword(id, password);
        return integer==1?new Res<>().success():new Res<>().fail(Message.PASSWORD_EDIT_FAILED);
    }

    @GetMapping("/classes")//查所有班级
    public Res selectClasses(){
        List<Classes> classes = adminService.selectClasses();
        return new Res().success(classes);
    }

    @GetMapping("/students/{class}")//按班级查看学生
    public Res selectByClass(@PathVariable("class") Integer clazz){
        List<Student> students = adminService.selectStuByClass(clazz);
        return new Res<>().success(students);
    }

    @GetMapping("/teacher") //分页查全部老师
    public Res selectTeacher(int current ,int size){
        PageResult pageResult = adminService.checkTeacherInfo(current, size);
        return new Res<>().success(pageResult);
    }

    @GetMapping("/teacher/byName/{name}") //分页按名字查老师（模糊）
    public Res selectTeacherByName(@PathVariable String name,int current,int size){
        PageResult pageResult = adminService.selectTeacherByName(name,current,size);
        return new Res<>().success(pageResult);
    }

    @GetMapping("/teacher/byId/{id}") //按ID查老师
    public Res selectTeacherById(@PathVariable long id){
        Teacher teacher = adminService.selectTeacherById(id);
        return new Res<>().success(teacher);
    }

    @GetMapping("/courses") //查全部课程
    public Res selectCourses(){
        return new Res<>().success(adminService.checkAllCourses());
    }

    @GetMapping("/courses/{courseName}") //查某课程信息
    public Res selectCourseByName(@PathVariable String courseName){
        Course course = adminService.selectOneCourse(courseName);
        return new Res<>().success(course);
    }

    @PostMapping("/courses") //新增课程
    public Res insertCourse(@RequestBody Course course){
        boolean insert = adminService.insertCourse(course);
        return insert==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @DeleteMapping("/courses/{courseName}") //删除课程
    public Res deleteCourse(@PathVariable String courseName){
        boolean delete = adminService.deleteCourse(courseName);
        return delete==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @PutMapping("/courses/scheduling/{courseName}") //排课
    public Res scheduling(@PathVariable String courseName,int day,int time){
        boolean scheduling = adminService.Scheduling(day, time, courseName);
        return scheduling==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @GetMapping("/teachers/schedule/{teacherName}")//查看老师课表
    public Res checkTeacherSchedule(@PathVariable String teacherName){
        List<TeacherCourse> TeacherSchedules = adminService.checkScheduleByTeacher(teacherName);
        return new Res().success(TeacherSchedules);
    }

    @PostMapping("/students") //新增学生
    public Res insertStu(@RequestBody Student student){
        boolean insert = adminService.insertsStu(student);
        return insert==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @DeleteMapping("/students/{id}") //删除学生（谨慎）
    public Res deleteStu(@PathVariable long id){
        boolean delete = adminService.deleteStu(id);
        return delete==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @PostMapping("/teachers") //新增教师
    public Res insertTeacher(@RequestBody Teacher teacher){
        boolean insert = adminService.insertTeacher(teacher);
        return insert==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @DeleteMapping("/teachers/{id}") //删除教师（谨慎）
    public Res deleteTeacher(@PathVariable long id){
        boolean delete = adminService.deleteTeacher(id);
        return delete==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @PostMapping("/import/students")
    public Res importStudents(@RequestParam("file")MultipartFile file) throws IOException {
        boolean importRes=adminService.importStudents(file.getInputStream());
        return importRes==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);

    }
}
