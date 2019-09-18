package org.xxpay.shop.module.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.shop.module.modle.WxPayLog;
import org.xxpay.shop.module.repository.WxPayLogRepository;
import org.xxpay.shop.module.service.WxPayLogService;

@Service
public class WxPayLogServiceImpl implements WxPayLogService {
    @Autowired
    WxPayLogRepository wxPayLogRepository;
    @Override
    public void doSave(String content) {
        WxPayLog wxPayLog = new WxPayLog();
        wxPayLog.setContent(content);
        wxPayLogRepository.save(wxPayLog);
    }
}
