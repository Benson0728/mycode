package com.grade.controller;

import com.grade.context.Message;
import com.grade.pojo.*;
import com.grade.result.LoginRes;
import com.grade.result.Res;
import com.grade.service.TeacherService;
import com.grade.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    @PostMapping ("/login")   //教师登录
    public LoginRes teacherLogin(long id, String password){
        Teacher teacher = teacherService.selectById(id);
        log.info("{}登陆了",id);
        boolean check=password.equals(teacher.getPassword());
        if (check==true)return new LoginRes<>().success(JwtUtils.createToken(teacher.getId(),teacher.getName()));
        return new LoginRes<>().fail();
    }

    @GetMapping //显示教师个人信息
    public Res selectInfo(){
        Claims claims = JwtUtils.getClaims();
        long id=Long.valueOf(claims.get("ID").toString());
        Teacher teacher = teacherService.selectById(id);
        return new Res<>().success(teacher);
    }

    @PutMapping  //修改密码
    public Res updatePassword(String password){
        Claims claims = JwtUtils.getClaims();
        long id= Long.valueOf(claims.get("ID").toString());
        Integer integer = teacherService.updatePassword(id, password);
        return integer==1?new Res<>().success():new Res<>().fail(Message.PASSWORD_EDIT_FAILED);
    }

    @GetMapping("/grades") //查询自己所教科目成绩
    public Res checkGrades(){
        Claims claims = JwtUtils.getClaims();
        String teacherName= (String) claims.get("username");
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

    @GetMapping("/schedule") //查看课表
    public Res checkSchedule(){
        Claims claims = JwtUtils.getClaims();
        String teacherName= (String) claims.get("username");
        List<TeacherCourse> schedule = teacherService.checkSchedule(teacherName);
        return new Res<>().success(schedule);
    }

    @PutMapping("/common/{course}/{stuId}") //录入平时成绩
    public Res enterCommonGrades(@PathVariable("course") String course,@PathVariable("stuId") long stuId,Integer grade){
        boolean enter = teacherService.enterCommonGrades(stuId, course, grade);
        return enter==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @PutMapping("/final/{course}/{stuId}") //录入期末成绩
    public Res enterFinalGrades(@PathVariable("course") String course,@PathVariable("stuId") long stuId,Integer grade){
        boolean enter = teacherService.enterFinalGrades(stuId, course, grade);
        return enter==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @PutMapping("/generate/student/{course}/{stuId}") //计算总成绩
    public Res generateGrades(@PathVariable("course") String course,@PathVariable("stuId") long stuId){
        boolean generate = teacherService.generateGrades(stuId, course);
        return generate==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @PutMapping("/import/common") //excel导入平时成绩
    public Res importCommonGrades(@RequestParam("file")MultipartFile file) throws IOException {
        boolean isImport = teacherService.excelImportCommonGrades(file.getInputStream());
        return isImport==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @PutMapping("/import/final") //excel导入平时成绩
    public Res importFinalGrades(@RequestParam("file")MultipartFile file) throws IOException {
        boolean isImport = teacherService.excelImportFinalGrades(file.getInputStream());
        return isImport==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);
    }

    @PutMapping("/generate/class/{class}/{course}") //班级科目平均成绩
    public Res generateClassGrades(@PathVariable("class") Integer clazz,@PathVariable("course") String courseName){
        boolean generate = teacherService.generateClassGrades(clazz, courseName);
        return generate==true?new Res<>().success():new Res<>().fail(Message.OPERATION_FAILED);

    }

    @PostMapping("/images")
    public Res saveImages(@RequestParam("file") MultipartFile file){
        Claims claims = JwtUtils.getClaims();
        if(file.isEmpty()){return new Res<>().fail("图片为空");}
        String username =(String) claims.get("username");
        File folder=new File(System.getProperty("user.dir")+File.separator+username+File.separator+"头像"+File.separator+username+".jpg");
        teacherService.savePhotoPath(username,folder.getPath());
        if(!folder.exists()){folder.mkdirs();}
        try {
            file.transferTo(folder);
            return new Res<>().success("文件上传成功");
        } catch (IOException e) {
           e.printStackTrace();
           return new Res<>().fail("文件上传失败");
        }
    }

    @GetMapping("/getImages")
    public Res getImages() {
        String username = (String) JwtUtils.getClaims().get("username");
        String photoPath = teacherService.getTeacherPhoto(username);
//        File photo=new File(photoPath);
        try{
//        FileInputStream fileInputStream = new FileInputStream(photo);
//        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
//        byte[] buffer=new byte[4096];
//        int bytesRead;
//        while ((bytesRead=fileInputStream.read(buffer))!=-1){
//            byteArrayOutputStream.write(buffer,0,bytesRead);
//        }
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//
//        fileInputStream.close();
//        byteArrayOutputStream.close();

            byte[] bytes = Files.readAllBytes(Path.of(photoPath));
            String base64Image = java.util.Base64.getEncoder().encodeToString(bytes);

            return new Res<>().success(base64Image);}
        catch (IOException e){
            return new Res<>().fail("获取图片失败");
        }
    }

}


