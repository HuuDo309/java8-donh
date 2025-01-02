package com.digidinos.shopping.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.digidinos.shopping.form.UserForm;

@Component
public class UserFormValidator implements Validator {

    private EmailValidator emailValidator = EmailValidator.getInstance();

    // Validator này chỉ dùng để kiểm tra đối với UserForm.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserForm.class; 
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm userForm = (UserForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty.userForm.userName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.userForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "NotEmpty.userForm.fullName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.userForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.userForm.phone");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.userForm.address");

        if (userForm.getUserName().length() > 0 && (userForm.getUserName().length() < 5 || userForm.getUserName().length() > 20)) {
            errors.rejectValue("userName", "Size.userForm.userName");
        }

        if (userForm.getPassword().length() > 0 && userForm.getPassword().length() < 6) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (userForm.getEmail().length() > 0 && !emailValidator.isValid(userForm.getEmail())) {
            errors.rejectValue("email", "Pattern.userForm.email");
        }

    }
}