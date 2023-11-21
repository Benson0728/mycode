package com.grade.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grade.mapper.*;
import com.grade.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TeacherService {
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    StuCourseTeacherMapper stuCourseTeacherMapper;
    @Autowired
    ClassGradesMapper classGradesMapper;
    @Autowired
    StuGradesMapper stuGradesMapper;

    /**
     * 查老师信息
     * @param id
     * @return
     */
    public Teacher selectById(long id){
        Teacher teacher = teacherMapper.selectById(id);
        return teacher;
    }


    /**
     * 修改密码
     * @param id
     * @param password
     * @return
     */
    public Integer updatePassword(long id,String password){
        UpdateWrapper<Teacher> updateWrapper=new UpdateWrapper<Teacher>();
        updateWrapper.eq("teacher_id",id)
                .set("password",password);
        return teacherMapper.update(null, updateWrapper);
    }

    /**
     * 查询自己所教科目成绩
     * @param teacherName
     * @return
     */
    public List<Course> selectGrades(String teacherName){
        QueryWrapper<Course> qw=new QueryWrapper<Course>()
                .eq("teacher_name",teacherName);
        List<Course> list = courseMapper.selectList(qw);
        return list;
    }



    /**
     * 根据学号查询成绩
     * @param stuId
     * @param courseName
     * @return
     */
    public StuGrades  selectGrades(long stuId,String courseName){
        QueryWrapper<StuGrades> qw =new QueryWrapper<StuGrades>()
                        .eq("course_name",courseName)
                        .eq("student_id",stuId);
        return stuGradesMapper.selectOne(qw);
    }

    /**
     * 根据学生名字查成绩
     * @param stuName
     * @param courseName
     * @return
     */
    public PageResult selectGrades(String stuName,String courseName,int current,int size){
        IPage<StuGrades> page=new Page(current,size);
        QueryWrapper<StuGrades> qw=new QueryWrapper<StuGrades>()
                .eq("course_name",courseName)
                .like("student_name",stuName)
                        .orderByDesc("grades");
        stuGradesMapper.selectPage(page,qw);
        return new PageResult(page.getRecords(),page.getTotal());
    }


    /**
     * 查询班级成绩
     * @param clazz
     * @param courseName
     * @return
     */
    public ClassGrades selectClassGrades(Integer clazz,String courseName){
        QueryWrapper<ClassGrades> qw=new QueryWrapper<ClassGrades>()
                .eq("class",clazz)
                .eq("course_name",courseName);
        return classGradesMapper.selectOne(qw);
    }

    /**
     * 查询所教科目班级排名
     * @param courseName
     * @return
     */
    public PageResult selectClassGrades(String courseName,int current,int size){
        IPage<ClassGrades> page=new Page(current,size);
        QueryWrapper<ClassGrades> qw=new QueryWrapper<ClassGrades>()
                .eq("course_name",courseName)
                        .orderByDesc("ave_grades");
        classGradesMapper.selectPage(page,qw);
        return new PageResult(page.getRecords(),page.getTotal());
    }

    /**
     * 班级内学生成绩排序
     * @param clazz
     * @param courseName
     * @return
     */
    public PageResult classStuRank(Integer clazz, String courseName,int current,int size){
        IPage<StuGrades> page=new Page(current,size);
        QueryWrapper<StuGrades> qw=new QueryWrapper<StuGrades>()
                                .eq("class",clazz)
                                .eq("course_name",courseName)
                                .orderByDesc("grades");
        stuGradesMapper.selectPage(page,qw);
        return new PageResult(page.getRecords(),page.getTotal());
    }

    /**
     * 所有学生成绩排名
     *
     * @param courseName
     * @return
     */
    public PageResult allStuRank(String courseName,int current,int size){
        IPage<StuGrades> page=new Page(current,size);
        QueryWrapper<StuGrades> qw=new QueryWrapper<StuGrades>()
                .eq("course_name", courseName)
                .orderByDesc("grades");

        stuGradesMapper.selectPage(page, qw);
        return new PageResult(page.getRecords(),page.getTotal());
    }


    /**
     * 课表查询
     * @param teacherName
     * @return
     */
    public List<StuCourseTeacher> checkSchedule(String teacherName){
        QueryWrapper<StuCourseTeacher> qw=new QueryWrapper<StuCourseTeacher>()
                        .select("course_name","day","time")
                        .eq("teacher_name",teacherName);
        return stuCourseTeacherMapper.selectList(qw);
    }
 }
