package org.xxpay.shop.module.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.xxpay.shop.module.modle.User;

public interface UserRepository extends JpaRepository<User,String> {
    User findByAdminId(Long adminId);
}
