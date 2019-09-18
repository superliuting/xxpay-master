package org.xxpay.shop.module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.xxpay.shop.module.modle.Payment;

public interface PaymentRepository extends JpaRepository<Payment,String> {
    @Query(value="select * from ss_payment where out_trade_no=?1", nativeQuery = true)
    Payment findPaymentByOutTradeNo(String out_trade_no);
}
