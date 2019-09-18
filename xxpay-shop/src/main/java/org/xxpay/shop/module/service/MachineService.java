package org.xxpay.shop.module.service;
import org.xxpay.shop.module.modle.Machine;

public interface MachineService {
    Machine findByImei(String imei);
    void doSave(Machine terminal);
}
