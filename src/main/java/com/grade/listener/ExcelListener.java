package com.grade.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.grade.mapper.StuMapper;
import com.grade.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener extends AnalysisEventListener<Student> {

    @Autowired
    StuMapper stuMapper;
    private List<Student> data = new ArrayList<>();
    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        data.add(student);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }

    public List<Student> getData(){
        return data;
    }
}
