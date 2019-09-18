package org.xxpay.shop.module.modle;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="ss_payment")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long paymentId;
    private String outTradeNo;
    private String payTradeNo;
    private double money;
    private int status;
    private String type;
    private long createTime;
    private String clientIp;
    private String productName;
    private long adminId;
    private long classifyId;
    private long terminalId;
    private long payTime;
}
