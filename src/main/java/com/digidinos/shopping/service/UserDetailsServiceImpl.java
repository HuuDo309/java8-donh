package com.digidinos.shopping.service;

import java.util.ArrayList;
import java.util.List;

import com.digidinos.shopping.dao.UserDAO;
import com.digidinos.shopping.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	User user = userDAO.findUserByUserName(username); 
        System.out.println("User= " + user);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        String role = user.getUserRole();

        List<GrantedAuthority> grantList = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        grantList.add(authority);

        boolean enabled = user.isActive();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUserName(), user.getEncrytedPassword(), enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked, grantList);

        return userDetails;
    }

}
