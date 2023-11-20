package com.grade.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class PageResult implements Serializable {
    private List list;
    private long total;
}
