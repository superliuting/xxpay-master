package org.xxpay.shop.module.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.xxpay.shop.module.modle.Terminal;

public interface TerminalRepository extends JpaRepository<Terminal,String> {
    Terminal findByTerminalId(Long terminalId);
}
