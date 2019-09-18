package org.xxpay.shop.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.shop.module.modle.Classify;
import org.xxpay.shop.module.repository.ClassifyRepository;
import org.xxpay.shop.module.service.ClassifyService;

import java.util.List;

@Service
public class ClassifyServiceImpl implements ClassifyService {
	@Autowired
	ClassifyRepository classifyRepository;

	@Override
	public Classify findClassifyByClId(Long cl_id) {
		return classifyRepository.findClassifyByClId(cl_id);
	}

	@Override
	public List<Classify> findClassifyByTerminalId(Long t_id) {
		return classifyRepository.findClassifyByTerminalId(t_id);
	}

	@Override
	public void doSave(Classify classify) {
		classifyRepository.save(classify);
	}

}
