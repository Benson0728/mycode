package com.XiaoDai.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("loanapplication")
public class LoanApplication implements Serializable {
    @TableId(value = "application_id",type = IdType.AUTO)
    private long applicationId;
    private long userId;
    private long amount;
    private double interestRate;
    private String status;
    private Date applicationDate;
}
