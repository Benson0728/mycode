package com.XiaoDai.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("loantransaction")
public class LoanTransaction implements Serializable {
    @TableId(value = "transaction_id",type = IdType.AUTO)
    private long transactionId;
    private long applicationId;
    private Date transactionDate;
    private long transactionAmount;

}
