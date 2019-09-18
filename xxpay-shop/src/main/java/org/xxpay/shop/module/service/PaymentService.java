package org.xxpay.shop.module.service;


import org.xxpay.shop.module.modle.Payment;

public interface PaymentService {
    void doSave(Payment payment);
    Payment findPaymentByOutTradeNo(String order_sn);
}
