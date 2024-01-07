package com.grade.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grade.listener.StuCommonExcelListener;
import com.grade.listener.StuFinalExcelListener;
import com.grade.mapper.*;
import com.grade.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.InputStream;
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
    @Autowired
    TeacherCourseMapper teacherCourseMapper;
    @Autowired
    TeacherPhotoMapper teacherPhotoMapper;

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

    public List<TeacherCourse> checkSchedule(String teacherName){
        QueryWrapper<TeacherCourse> qw=new QueryWrapper<TeacherCourse>()
                        .select("course_name","day","time")
                        .eq("teacher_name",teacherName);
        return teacherCourseMapper.selectList(qw);
    }

    /**
     * 录入平时成绩
     * @param stuID
     * @param courseName
     * @param grade
     * @return
     */
    public boolean enterCommonGrades(long stuID,String courseName,Integer grade){
        try {
        UpdateWrapper<StuGrades> uw=new UpdateWrapper<StuGrades>()
                .eq("student_id",stuID)
                .eq("course_name",courseName)
                .setSql("common_grades ="+grade);
        stuGradesMapper.update(null,uw);
        }catch (Exception e){
                e.printStackTrace();
                return false;}
                 return true;
    }

    /**
     * 录入期末成绩
     * @param stuID
     * @param courseName
     * @param grade
     * @return
     */
    public boolean enterFinalGrades(long stuID,String courseName,Integer grade){
        try {
            UpdateWrapper<StuGrades> uw=new UpdateWrapper<StuGrades>()
                    .eq("student_id",stuID)
                    .eq("course_name",courseName)
                    .setSql("final_grades ="+ grade);
            stuGradesMapper.update(null,uw);
        }catch (Exception e){
            e.printStackTrace();
            return false;}
        return true;
    }

    /**
     * 计算总成绩
     * @param stuId
     * @param courseName
     * @return
     */
    public boolean generateGrades(long stuId,String courseName){
        try {
        QueryWrapper<StuGrades> qw=new QueryWrapper<StuGrades>()
                .eq("student_id",stuId)
                .eq("course_name",courseName);
        StuGrades stuGrades = stuGradesMapper.selectOne(qw);
        Integer commonGrades = stuGrades.getCommonGrades();
        Integer finalGrades = stuGrades.getFinalGrades();
        if (commonGrades!=0&& finalGrades !=0){
            Course course = courseMapper.selectOne(new QueryWrapper<Course>()
                    .eq("course_name", courseName));
            float grades=course.getCommonGradePercent()*commonGrades+course.getFinalGradePercent()* finalGrades;
            stuGradesMapper.update(null,new UpdateWrapper<StuGrades>()
                    .eq("student_id",stuId)
                    .eq("course_name",courseName)
                    .setSql("grades ="+grades));
            return true;
        }
      }catch (Exception e){
               e.printStackTrace();
               return false;
        }
               return false;
    }

    /**
     * excel导入平时成绩
     * @param inputStream
     * @return
     */
    public boolean excelImportCommonGrades(InputStream inputStream){
        try {
            StuCommonExcelListener stuCommonExcelListener=new StuCommonExcelListener();
            EasyExcel.read(inputStream, StuCommon.class,stuCommonExcelListener).sheet().doRead();
            List<StuCommon> commonList=stuCommonExcelListener.getData();
            for (StuCommon stuCommon : commonList) {
                long id = stuCommon.getId();
                String courseName = stuCommon.getCourseName();
                Integer commonGrades = stuCommon.getCommonGrades();
                UpdateWrapper<StuGrades> uw=new UpdateWrapper<StuGrades>()
                        .eq("student_id",id)
                        .eq("course_name",courseName)
                        .setSql("common_grades ="+commonGrades);
                stuGradesMapper.update(null,uw);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * excel导入期末成绩
     * @param inputStream
     * @return
     */
    public boolean excelImportFinalGrades(InputStream inputStream){
        try {
            StuFinalExcelListener stuFinalExcelListener=new StuFinalExcelListener();
            EasyExcel.read(inputStream, StuFinal.class,stuFinalExcelListener).sheet().doRead();
            List<StuFinal> finalList=stuFinalExcelListener.getData();
            for (StuFinal stuFinal : finalList) {
                long id = stuFinal.getId();
                String courseName = stuFinal.getCourseName();
                Integer finalGrades = stuFinal.getFinalGrades();
                UpdateWrapper<StuGrades> uw=new UpdateWrapper<StuGrades>()
                        .eq("student_id",id)
                        .eq("course_name",courseName)
                        .setSql("final_grades ="+finalGrades);
                stuGradesMapper.update(null,uw);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 生成班级科目总成绩
     * @param clazz
     * @param courseName
     * @return
     */
    public boolean generateClassGrades(Integer clazz,String courseName){
        try {
            float total = 0;
            Integer num = 0;
            QueryWrapper<StuGrades> qw = new QueryWrapper<StuGrades>()
                    .eq("class", clazz)
                    .eq("course_name", courseName);
            List<StuGrades> list = stuGradesMapper.selectList(qw);
            for (StuGrades stuGrades : list) {
                float grades = stuGrades.getGrades();
                total += grades;
                num++;
            }
            float ave = total / num;
            ClassGrades cg = new ClassGrades(clazz, courseName, ave);
            classGradesMapper.insert(cg);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 将图片路径保存到数据库
     * @param teacherName
     * @param path
     * @return int为1表示成功
     */
    public int savePhotoPath(String teacherName,String path){
        TeacherPhoto teacherPhoto=new TeacherPhoto(teacherName,path);
        int insert = teacherPhotoMapper.insert(teacherPhoto);
        return insert;
    }

    /**
     * 获取图片路径
     * @param teacherName
     * @return
     */
    public String getTeacherPhoto(String teacherName){
        QueryWrapper<TeacherPhoto> qw=new QueryWrapper<TeacherPhoto>()
                .eq("teachername",teacherName);
        String photoPath = teacherPhotoMapper.selectOne(qw).getPhotoPath();
        return photoPath;
    }
 }
