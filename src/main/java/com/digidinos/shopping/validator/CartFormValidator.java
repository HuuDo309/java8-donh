package com.digidinos.shopping.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.digidinos.shopping.model.CartInfo;
import com.digidinos.shopping.model.CartLineInfo;
import com.digidinos.shopping.repository.ProductRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartFormValidator implements Validator {

    @Autowired
    private ProductRepository productREPO;

    @Override
    public boolean supports(Class<?> clazz) {
        return CartInfo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CartInfo cartForm = (CartInfo) target;

        for (int i = 0; i < cartForm.getCartLines().size(); i++) {
            validateCartLine(cartForm.getCartLines().get(i), i, errors);
        }
    }

    private void validateCartLine(CartLineInfo cartLine, int index, Errors errors) {
        if (cartLine == null || cartLine.getProductInfo() == null || cartLine.getProductInfo().getCode() == null) {
            errors.rejectValue("cartLines[" + index + "].quantity", 
                    "InvalidCartLine.cartForm.quantity", 
                    "Invalid cart line");
            return;
        }

        int quantityInCart = cartLine.getQuantity();
        String productCode = cartLine.getProductInfo().getCode();

        int quantityInStock = Optional.ofNullable(productREPO.getStockQuantityByProductCode(productCode)).orElse(0);

        if (quantityInCart > quantityInStock) {
            errors.rejectValue("cartLines[" + index + "].quantity", 
                    "MaxQuantityExceeded.cartForm.quantity", 
                    new Object[]{quantityInStock}, 
                    "Quantity exceeds available stock: " + quantityInStock);
        }
    }
}
