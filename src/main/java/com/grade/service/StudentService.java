package com.grade.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grade.mapper.StuCourseTeacherMapper;
import com.grade.mapper.StuGradesMapper;
import com.grade.mapper.StuMapper;
import com.grade.pojo.PageResult;
import com.grade.pojo.StuCourseTeacher;
import com.grade.pojo.StuGrades;
import com.grade.pojo.Student;
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
//        Collections.sort(list, new Comparator<StuGrades>() {
//            @Override
//            public int compare(StuGrades o1, StuGrades o2) {
//                Integer age1= (int) o1.getGrades();
//                Integer age2= (int) o2.getGrades();
//                return  age1.compareTo(age2);
//            }
//        });
//        list.sort(new Comparator<StuGrades>() {
//            @Override
//            public int compare(StuGrades u1, StuGrades u2) {
//                Integer age1= (int)u1.getGrades();
//                Integer age2= (int)u2.getGrades();
//                return  age1.compareTo(age2);
//            }
//        });
//
//        //List接口的sort方法，lambda表达式写法
//        list.sort((u4,u5)->{
//            Integer age1=(int) u4.getGrades();
//            Integer age2=(int)u5.getGrades();
//            return  age1.compareTo(age2);
//        });
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
}
