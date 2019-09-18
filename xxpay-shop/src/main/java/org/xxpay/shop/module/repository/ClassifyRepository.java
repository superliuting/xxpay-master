package org.xxpay.shop.module.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.xxpay.shop.module.modle.Classify;

import java.util.List;

public interface ClassifyRepository extends JpaRepository<Classify,String> {

    @Query(value="select * from ss_classify t where t.cl_id=?1 and  t.status=1 and t.stock>0", nativeQuery = true)
    Classify findClassifyByClId(Long cl_id);

    @Query(value="select * from ss_classify t where t.terminal_id=?1 and  t.status=1", nativeQuery = true)
    List<Classify> findClassifyByTerminalId(Long cl_id);
}
