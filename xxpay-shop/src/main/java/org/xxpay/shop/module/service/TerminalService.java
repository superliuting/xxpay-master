package org.xxpay.shop.module.service;
import org.xxpay.shop.module.modle.Terminal;

public interface TerminalService {
    Terminal findByTerminalId(Long terminal_id);
    void doSave(Terminal terminal);
}
