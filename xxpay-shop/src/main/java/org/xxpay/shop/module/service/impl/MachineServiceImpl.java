package org.xxpay.shop.module.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.shop.module.modle.Machine;
import org.xxpay.shop.module.repository.MachineRepository;
import org.xxpay.shop.module.service.MachineService;

@Service
public class MachineServiceImpl implements MachineService {
    @Autowired
    MachineRepository machineRepository;

    @Override
    public Machine findByImei(String imei) {
        return machineRepository.findByImei(imei);
    }
    @Override
    public void doSave(Machine payment) {
        machineRepository.save(payment);
    }

}
