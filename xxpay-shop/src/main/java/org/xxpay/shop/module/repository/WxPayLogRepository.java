package org.xxpay.shop.module.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.xxpay.shop.module.modle.WxPayLog;

public interface WxPayLogRepository extends JpaRepository<WxPayLog,String> {
}
