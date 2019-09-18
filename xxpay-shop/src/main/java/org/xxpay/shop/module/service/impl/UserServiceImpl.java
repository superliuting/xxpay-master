package org.xxpay.shop.module.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.shop.module.modle.User;
import org.xxpay.shop.module.repository.UserRepository;
import org.xxpay.shop.module.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User findByAdminId(Long adminId) {
        return userRepository.findByAdminId(adminId);
    }

    @Override
    public void doSave(User user) {
        userRepository.save(user);
    }
}
