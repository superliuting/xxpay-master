package org.xxpay.shop.module.service.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.shop.module.modle.Payment;
import org.xxpay.shop.module.repository.PaymentRepository;
import org.xxpay.shop.module.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Override
    public void doSave(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public Payment findPaymentByOutTradeNo(String order_sn) {
        return paymentRepository.findPaymentByOutTradeNo(order_sn);
    }
}
