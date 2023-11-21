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
public class AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    StuMapper stuMapper;
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
    ClassMapper classMapper;

    /**
     * 查管理员
     * @param id
     * @return
     */
    public Admin selectById(long id){
        return adminMapper.selectById(id);
    }


    /**
     * 修改密码
     * @param id
     * @param password
     * @return
     */
    public Integer updatePassword(long id,String password){
        UpdateWrapper<Admin> updateWrapper=new UpdateWrapper<Admin>();
        updateWrapper.eq("admin_id",id)
                .set("password",password);
        return adminMapper.update(null, updateWrapper);
    }

    /**
     * 查班级
     * @return
     */
    public List<Classes> selectClasses(){
        return classMapper.selectList(null);
    }
    /**
     * 按班级查看学生
     * @param clazz
     * @return
     */
    public List<Student> selectStuByClass(Integer clazz){
        QueryWrapper<Student> qw=new QueryWrapper<Student>()
                .eq("class",clazz);
        return stuMapper.selectList(qw);
    }

    /**
     * 查老师
     * @return
     */
    public PageResult checkTeacherInfo(int current, int size){
        IPage  page=new Page(current,size);
        teacherMapper.selectPage(page,null);
        return new PageResult(page.getRecords(), page.getTotal());
    }

    /**
     * 按姓名查老师
     * @param name
     * @param current
     * @param size
     * @return
     */
    public PageResult selectTeacherByName(String name,int current,int size){
        QueryWrapper<Teacher> qw=new QueryWrapper<Teacher>()
                .like("name",name);
        IPage  page=new Page(current,size);
        teacherMapper.selectPage(page,qw);
        return new PageResult(page.getRecords(), page.getTotal());
    }

    /**
     * 按ID查老师
     * @param ID
     * @return
     */
    public Teacher selectTeacherById(long ID){
        return teacherMapper.selectById(ID);
    }

    /**
     * 查看所有课程
     * @return
     */
    public List<Course> checkAllCourses(){
        return courseMapper.selectList(null);
    }

    /**
     * 查某课程信息
     * @param courseName
     * @return
     */
    public Course selectOneCourse(String courseName){
        return courseMapper
                .selectOne(new QueryWrapper<Course>()
                        .eq("course_name", courseName));
    }

    /**
     * 新增课程
     * @param course
     * @return
     */
    public boolean insertCourse(Course course){
        int insert = courseMapper.insert(course);
        return insert==1?true:false;
    }

    /**
     * 删除课程（谨慎）
     * @param courseName
     * @return
     */
    public boolean deleteCourse(String courseName){
        try {
        QueryWrapper<Course> qw=new QueryWrapper<Course>()
                .eq("course_name",courseName);
        courseMapper.delete(qw);
        QueryWrapper<StuCourseTeacher> qw2=new QueryWrapper<StuCourseTeacher>()
                .eq("course_name",courseName);
        stuCourseTeacherMapper.delete(qw2);
        QueryWrapper<ClassGrades> qw3=new QueryWrapper<ClassGrades>()
                .eq("course_name",courseName);
        classGradesMapper.delete(qw3);
        return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 新课排课
     * @param day
     * @param time
     * @param courseName
     * @return
     */
    public boolean Scheduling(int day,int time,String courseName){
        Course course=selectOneCourse(courseName);
        String teacher=course.getTeacherName();
        QueryWrapper<StuCourseTeacher> qw=new QueryWrapper<StuCourseTeacher>()
                .eq("teacher_name",teacher);
        List<StuCourseTeacher> list = stuCourseTeacherMapper.selectList(qw);
        //查看老师方面是否撞课
        for (StuCourseTeacher courseTeacher : list) {
            if(day==courseTeacher.getDay()&&time==courseTeacher.getTime()){return false;}
        }
        StuCourseTeacher sct=new StuCourseTeacher();
        sct.setDay(day);
        sct.setTime(time);
        sct.setCourseName(courseName);
        sct.setTeacherName(teacher);
        stuCourseTeacherMapper.insert(sct);
        return true;
    }

    /**
     * 查某个老师的课表
     * @param teacherName
     * @return
     */
    public List<StuCourseTeacher> checkScheduleByTeacher(String teacherName){
        QueryWrapper<StuCourseTeacher> qw=new QueryWrapper<StuCourseTeacher>()
                .select("course_name","day","time")
                .eq("teacher_name",teacherName);
        return stuCourseTeacherMapper.selectList(qw);
    }

    /**
     * 增加学生
     * @param student
     * @return
     */
    public boolean insertsStu(Student student){
        Integer clazz=student.getClazz();
        student.setPassword("123456");//默认密码123456
        try{
        UpdateWrapper<Classes> uw=new UpdateWrapper<Classes>()
                .setSql("stu_num = stu_num + 1")
                .eq("class_num",clazz);
            classMapper.update(null,uw);//班级表人数加一
            stuMapper.insert(student);
        }catch (Exception e){
            return false;//学号已存在
        }
        return true;
    }

    /**
     * 删除学生（谨慎）
     * @param ID
     * @return
     */
    public boolean deleteStu(long ID){
        try {
            Integer clazz = stuMapper.selectById(ID).getClazz();
            stuMapper.deleteById(ID);
            QueryWrapper<StuCourseTeacher> qw1 = new QueryWrapper<StuCourseTeacher>()
                    .eq("student_id", ID);
            stuCourseTeacherMapper.delete(qw1);
            QueryWrapper<StuGrades> qw2 = new QueryWrapper<StuGrades>()
                    .eq("student_id", ID);
            stuGradesMapper.delete(qw2);
            UpdateWrapper<Classes> uw=new UpdateWrapper<Classes>()
                    .setSql("stu_num = stu_num - 1")
                    .eq("class_num",clazz);
            classMapper.update(null,uw);//班级表人数减一
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 新增教师
     * @param teacher
     * @return
     */
    public boolean insertTeacher(Teacher teacher){
        teacher.setPassword("123456");//默认密码123456
        try {
            teacherMapper.insert(teacher);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除教师（谨慎）
     * @param ID
     * @return
     */
    public boolean deleteTeacher(long ID){
        try {
            String name = teacherMapper.selectById(ID).getName();
            teacherMapper.deleteById(ID);
            QueryWrapper<StuCourseTeacher> qw1=new QueryWrapper<StuCourseTeacher>()
                .eq("teacher_name",name);
        stuCourseTeacherMapper.delete(qw1);
    }catch (Exception e){
        e.printStackTrace();
        return false;
        }
        return true;
    }
}
