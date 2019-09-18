package org.xxpay.shop.module.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.xxpay.shop.module.modle.RunCommand;

public interface RunCommandRepository extends JpaRepository<RunCommand,String> {
}
