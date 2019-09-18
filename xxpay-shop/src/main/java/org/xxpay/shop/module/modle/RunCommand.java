package org.xxpay.shop.module.modle;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity(name="ss_run_command")
@Data
public class RunCommand {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long runId;
    private String command;
    private Date createTime;
    private int isExec;
    private Date execTime;
    private String token;
    private long terminalId;
    private String remark;
}
