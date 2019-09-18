package org.xxpay.shop.module.modle;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="ss_terminal")
@Data
public class Terminal {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long terminalId;
    private String deviceNum;
    private String name;
    private int status;
    private String passwd;
    private String capacity;
    private long operator;
    private String ationing;
    private long sort;
    private long provinceId;
    private long cityId;
    private long createTime;
    private long areaId;
    private String pwdSalt;
}
