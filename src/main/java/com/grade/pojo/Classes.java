package com.grade.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("class")
public class Classes implements Serializable {
    @TableId("class_num")
    private int classNum;
    private int stuNum;
}
