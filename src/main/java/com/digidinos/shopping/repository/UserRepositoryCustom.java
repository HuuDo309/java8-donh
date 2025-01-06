package com.digidinos.shopping.repository;

import com.digidinos.shopping.entity.User;

public interface UserRepositoryCustom {
    User findUserByUserName(String userName);
}
