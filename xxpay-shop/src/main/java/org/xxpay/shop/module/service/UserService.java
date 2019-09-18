package org.xxpay.shop.module.service;
import org.xxpay.shop.module.modle.User;

public interface UserService {
    User findByAdminId(Long adminId);
    void doSave(User user);
}
