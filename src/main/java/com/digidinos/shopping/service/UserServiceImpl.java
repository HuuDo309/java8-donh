package com.digidinos.shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digidinos.shopping.dao.UserDAO;
import com.digidinos.shopping.entity.User;
import com.digidinos.shopping.form.UserForm;

@Service
public class UserServiceImpl {

	@Autowired
    private UserDAO userDAO; 

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public String register(UserForm userForm) {
        User user = new User();
        
        user.setUserName(userForm.getUserName());
        user.setEncrytedPassword(passwordEncoder.encode(userForm.getPassword())); 
        user.setFullName(userForm.getFullName());
        user.setEmail(userForm.getEmail());
        user.setPhone(userForm.getPhone());
        user.setAddress(userForm.getAddress());
        user.setUserRole("ROLE_CUSTOMER");
        user.setActive(true);

        userDAO.saveUser(user);
        return "User registered successfully!";
    }

    public User findUserByUserName(String userName) {
        return userDAO.findUserByUserName(userName);
    }
    
    public User findUserById(Integer id) {
    	return userDAO.findUserById(id);
    }
}
