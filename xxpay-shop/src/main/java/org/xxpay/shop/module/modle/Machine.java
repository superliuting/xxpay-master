package org.xxpay.shop.module.modle;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="ss_machine")
@Data
public class Machine {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    private String imei;
    private long createTime;
    private String token;
    private String mtoken;
}
