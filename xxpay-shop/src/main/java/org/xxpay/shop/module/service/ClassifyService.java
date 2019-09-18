package org.xxpay.shop.module.service;


import org.xxpay.shop.module.modle.Classify;

import java.util.List;

public interface ClassifyService {
	Classify findClassifyByClId(Long cl_id);

	List<Classify> findClassifyByTerminalId(Long cl_id);

	void doSave(Classify classify);
}
