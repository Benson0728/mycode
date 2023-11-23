package com.grade.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.grade.pojo.StuFinal;

import java.util.ArrayList;
import java.util.List;

public class StuFinalExcelListener extends AnalysisEventListener<StuFinal> {

    private List<StuFinal> data=new ArrayList<>();
    @Override
    public void invoke(StuFinal stuFinal, AnalysisContext analysisContext) {
        data.add(stuFinal);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<StuFinal> getData(){return data;}
}
