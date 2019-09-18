package org.xxpay.shop.module.modle;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="ss_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long userId;
    private String openid;
    private String nickname;
    private double commission;
    private int status;
    private long tokenExpiretime;
    private String headimgurl;
    private int isAgency;
    private long adminId;
    private String tel;
    private int sex;
    private long createTime;
    private long updateTime;
    private String address;
    private String idcard;
    private String royalty;
    private long machineNum;
    private String token;
    private long lastLogin;
    private long regTime;
    private String lastIp;
    private String account;
}
