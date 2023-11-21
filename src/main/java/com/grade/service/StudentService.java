package com.grade.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grade.mapper.CourseMapper;
import com.grade.mapper.StuCourseTeacherMapper;
import com.grade.mapper.StuGradesMapper;
import com.grade.mapper.StuMapper;
import com.grade.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    StuMapper stuMapper;
    @Autowired
    StuGradesMapper stuGradesMapper;
    @Autowired
    StuCourseTeacherMapper stuCourseTeacherMapper;
    @Autowired
    CourseMapper courseMapper;


    /**
     * 根据id查学生信息
     * @param id
     * @return
     */
    public Student selectById(Long id){
        Student student = stuMapper.selectById(id);
        return student;
    }

    /**
     * 根据学生姓名模糊查询
     * @param name
     * @return
     */
    public List<Student> selectByName(String name){
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name",name);
        return stuMapper.selectList(queryWrapper);
    }

    /**
     * 修改密码
     * @param id
     * @param password
     * @return
     */
    public Integer updatePassword(long id,String password){
        UpdateWrapper<Student> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("student_id",id)
                        .set("password",password);
        return stuMapper.update(null, updateWrapper);
    }

    /**
     * 查询某位学生成绩
     * @param id
     * @return
     */
    public List<StuGrades> checkGrades(long id){
        QueryWrapper<StuGrades> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("student_id",id);
        return stuGradesMapper.selectList(queryWrapper);
    }

    /**
     * 查学生某一科目成绩
     * @param id
     * @param course
     * @return
     */
    public StuGrades checkGrades(long id,String course){
        QueryWrapper<StuGrades> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("student_id",id);
        queryWrapper.eq("course_name",course);
        return stuGradesMapper.selectOne(queryWrapper);
    }

    /**
     * 查询某一科目所有学生成绩并排序
     * @param course
     * @return
     */
    public List<StuGrades> checkGrades(String course){
        QueryWrapper<StuGrades> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_name",course);
        List<StuGrades>  list=stuGradesMapper.selectList(queryWrapper);
        Comparator<StuGrades> gradesComparator=Comparator.comparing(StuGrades::getGrades);
        list.sort(gradesComparator.reversed());
        return list;

    }

    /**
     * 分页查询学生
     * @param current
     * @param size
     * @return
     */
    public PageResult selectPage(int current, int size) {
        IPage page=new Page(current,size);
        stuMapper.selectPage(page,null);
        return new PageResult(page.getRecords(),page.getTotal());
    }

    /**
     * 查询学生课表
     * @param id
     * @return
     */
    public List<StuCourseTeacher> checkSchedule(long id){
        QueryWrapper<StuCourseTeacher> qw =new QueryWrapper<StuCourseTeacher>()
                .eq("student_id",id);
        return stuCourseTeacherMapper.selectList(qw);
    }

    /**
     * 学生排课
     * @param day
     * @param time
     * @param courseName
     * @param stuID
     * @param stuName
     * @return
     */
    public boolean chooseCourses(int day,int time,String courseName,long stuID,String stuName){
        try {
            QueryWrapper<StuCourseTeacher> qw1 = new QueryWrapper<StuCourseTeacher>()
                    .eq("student_id", stuID);
            List<StuCourseTeacher> list = stuCourseTeacherMapper.selectList(qw1);
            for (StuCourseTeacher StuCourse : list) {
                if (StuCourse.getDay() == day && StuCourse.getTime() == time) {
                    return false;//撞课
                }
            }
            QueryWrapper<Course> qw2 = new QueryWrapper<Course>()
                    .eq("course_name", courseName);
            Course course = courseMapper.selectOne(qw2);
            String teacherName = course.getTeacherName();
            StuCourseTeacher sct = new StuCourseTeacher(stuID, stuName, courseName, day, time, teacherName);
            stuCourseTeacherMapper.insert(sct);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
