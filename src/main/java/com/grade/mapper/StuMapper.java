package com.grade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.grade.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StuMapper extends BaseMapper<Student> {
}
