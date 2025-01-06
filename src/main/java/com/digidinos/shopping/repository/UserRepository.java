package com.digidinos.shopping.repository;

import com.digidinos.shopping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    User findByUserName(String userName);
}
