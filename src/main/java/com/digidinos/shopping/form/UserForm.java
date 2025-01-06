package com.digidinos.shopping.form;

import com.digidinos.shopping.entity.User;

public class UserForm {

	private String userName;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String address;

    private boolean valid;

    public UserForm() {
    }

    public UserForm(User user) {
        if (user != null) {
            this.userName = user.getUserName();
            this.fullName = user.getFullName();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.address = user.getAddress();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
}
