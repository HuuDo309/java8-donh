package com.digidinos.shopping.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.digidinos.shopping.entity.User;

@Transactional
@Repository
public class UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void saveUser(User user) {
	    Session session = sessionFactory.getCurrentSession();
	    session.saveOrUpdate(user);
	} 
	
	public User findUserById(Integer id) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(User.class, id);
	}
	
	public User findUserByUserName(String userName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM User WHERE userName = :userName", User.class)
                      .setParameter("userName", userName)
                      .uniqueResult();
    }
	
}
