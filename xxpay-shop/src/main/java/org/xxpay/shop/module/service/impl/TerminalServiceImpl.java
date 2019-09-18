package org.xxpay.shop.module.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.shop.module.modle.Terminal;
import org.xxpay.shop.module.repository.TerminalRepository;
import org.xxpay.shop.module.service.TerminalService;

@Service
public class TerminalServiceImpl implements TerminalService {
    @Autowired
    TerminalRepository terminalRepository;

    @Override
    public Terminal findByTerminalId(Long terminalId) {
        return terminalRepository.findByTerminalId(terminalId);
    }

    @Override
    public void doSave(Terminal terminal) {
        terminalRepository.save(terminal);
    }
}
