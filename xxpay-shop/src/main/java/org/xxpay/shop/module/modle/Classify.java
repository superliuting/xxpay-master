package org.xxpay.shop.module.modle;


import lombok.Data;

import javax.persistence.*;

@Entity(name="ss_classify")
@Data
public class Classify {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long clId;
    private String name;
    private String num;
    private int status;
    private long sort;
    private String icon;
    private double price;
    private long operator;
    private long terminalId;
    private String memo;
    private long stock;
    private int type;
    private long createTime;
    private int road;
    private double cost;
}
