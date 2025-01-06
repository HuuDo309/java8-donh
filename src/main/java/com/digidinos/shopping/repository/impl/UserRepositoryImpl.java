package com.digidinos.shopping.repository.impl;

import com.digidinos.shopping.entity.User;
import com.digidinos.shopping.repository.UserRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findUserByUserName(String userName) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE userName = :userName", User.class);
        query.setParameter("userName", userName);
        return query.getSingleResult();
    }
}
