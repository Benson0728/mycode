package com.grade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.grade.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
