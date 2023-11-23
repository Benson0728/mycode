package com.grade.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.grade.pojo.StuCommon;

import javax.print.attribute.standard.OrientationRequested;
import java.util.ArrayList;
import java.util.List;

public class StuCommonExcelListener extends AnalysisEventListener<StuCommon> {

    private List<StuCommon> data=new ArrayList<>();
    @Override
    public void invoke(StuCommon stuCommon, AnalysisContext analysisContext) {
        data.add(stuCommon);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<StuCommon> getData(){return data;}
}
