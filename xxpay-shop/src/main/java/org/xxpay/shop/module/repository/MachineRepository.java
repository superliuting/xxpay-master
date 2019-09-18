package org.xxpay.shop.module.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.xxpay.shop.module.modle.Machine;

public interface MachineRepository extends JpaRepository<Machine,String> {
    Machine findByImei(String imei);
}
