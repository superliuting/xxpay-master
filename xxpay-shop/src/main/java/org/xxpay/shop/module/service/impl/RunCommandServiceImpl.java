package org.xxpay.shop.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.shop.module.modle.RunCommand;
import org.xxpay.shop.module.repository.RunCommandRepository;
import org.xxpay.shop.module.service.RunCommandService;

@Service
public class RunCommandServiceImpl implements RunCommandService {
	@Autowired
	RunCommandRepository runCommandRepository;

	@Override
	public void doSave(RunCommand runCommand) {
		runCommandRepository.save(runCommand);
	}

}
