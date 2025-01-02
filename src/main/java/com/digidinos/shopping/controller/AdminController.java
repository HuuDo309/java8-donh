package com.digidinos.shopping.controller;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import com.digidinos.shopping.dao.OrderDAO;
import com.digidinos.shopping.dao.ProductDAO;
import com.digidinos.shopping.entity.Order;
import com.digidinos.shopping.entity.OrderDetail;
import com.digidinos.shopping.entity.Product;
import com.digidinos.shopping.form.ProductForm;
import com.digidinos.shopping.form.UserForm;
import com.digidinos.shopping.model.OrderDetailInfo;
import com.digidinos.shopping.model.OrderInfo;
import com.digidinos.shopping.pagination.PaginationResult;
import com.digidinos.shopping.service.UserServiceImpl;
import com.digidinos.shopping.validator.ProductFormValidator;
import com.digidinos.shopping.validator.UserFormValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Transactional
public class AdminController {

	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private UserFormValidator userFormValidator;
	
	@Autowired
	private ProductFormValidator productFormValidator;
	
	@Autowired
	private UserServiceImpl userService;
	
	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target = " + target);
		
		if (target.getClass() == ProductForm.class) {
			dataBinder.setValidator(productFormValidator);
		}
		
		if (target.getClass() == UserForm.class) {
			dataBinder.setValidator(userFormValidator);
		}
	}
	
	@GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userForm", new UserForm()); 
        return "register";
    }
	
	 @PostMapping("/register")
	    public String register(
	    		@ModelAttribute("userForm") @Validated UserForm userForm,
	            BindingResult result,
	            Model model) {

		 if (result.hasErrors()) {
			 userForm.setValid(false); 
		     return "register"; 
		 }
		 
	     if (userService.findUserByUserName(userForm.getUserName()) != null) {
	         model.addAttribute("error", "Tên đăng nhập đã tồn tại.");
	         return "register";
	     }

	     userForm.setValid(true);
	     userService.register(userForm);

	     return "redirect:/login";   
	     
	}
	
	//GET: Hiển thị trang login
	@GetMapping({ "/login" }) 
	public String login(Model model) {
		
		return "login";
	}
	
	@GetMapping({ "/accountInfo" })
	public String accountInfo(Model model) {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(userDetails.getPassword());
		System.out.println(userDetails.getUsername());
		System.out.println(userDetails.isEnabled());
		
		model.addAttribute("userDetails", userDetails);
		return "accountInfo";
	}
	
	@GetMapping({ "/admin/orderList" })
	public String orderList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		int page =1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
			
		}
		final int MAX_RESULT =5;
		final int MAX_NAVIGATION_PAGE =10;
		
		PaginationResult<OrderInfo> paginationResult //
				= orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);
		
		model.addAttribute("paginationResult", paginationResult);
		return "orderList";
	}
	
	// GET: Hiển thị product.
	@GetMapping({ "/admin/product" })
	public String product(Model model, @RequestParam(value = "code", defaultValue ="") String code) {
		ProductForm productForm = null;
		
		if (code != null && code.length() > 0) {
			Product product = productDAO.findProduct(code);
			if (product != null) {
				productForm = new ProductForm(product);
			}
		}
		if (productForm == null) {
			productForm = new ProductForm();
			productForm.setNewProduct(true);
		}
		model.addAttribute("productForm", productForm);
		return "product";
	}
	
	// POST: Save product
	@PostMapping({ "/admin/product" })
	public String productSave(Model model, //
			@ModelAttribute("productForm") @Validated ProductForm productForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			return "product";
		}
		try {
			productDAO.save(productForm);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "product";
		}
		
		return "redirect:/productList";
	}
	
	 // POST: Xóa sản phẩm
    @PostMapping("/admin/product/delete")
    public String deleteProduct(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        try {
            Product product = productDAO.findProduct(code);
            if (product == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
                return "redirect:/admin/product";
            }

            productDAO.delete(product);

            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully.");
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + message);
        }

        return "redirect:/productList";  
    }
	
	// GET: Hiển thị order.
	@GetMapping({ "/admin/order" })
	public String orderView(Model model, @RequestParam("orderId") String orderId) {
		OrderInfo orderInfo = null;
	    if (orderId != null) {
	        orderInfo = this.orderDAO.getOrderInfo(orderId);
	    }
	    if (orderInfo == null) {
	        return "redirect:/admin/orderList";
	    }

	    String orderStatus = orderDAO.getOrderStatus(orderId);

	    model.addAttribute("orderStatus", orderStatus);
	    
	    List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
	    orderInfo.setDetails(details);
	    
	    model.addAttribute("orderInfo", orderInfo);
	    
	    return "order";
	}
	
	@PostMapping({ "/updateStatus" })
    public String updateOrderStatus(@RequestParam("orderId") String orderId, 
                                    @RequestParam("status") String status,
                                    Authentication authentication) {
		try {
	        Order order = orderDAO.findOrder(orderId);
	        
	        if (order != null) {
	            String currentStatus = order.getStatus();
	            
	            if ("Đã xác nhận".equals(currentStatus)) {
	                if (!status.equals(currentStatus)) {
	                    orderDAO.updateOrderStatus(orderId, status);
	                }
	                return "redirect:/admin/order?orderId=" + orderId;
	            }
	            
	            if ("Đã xác nhận".equals(status)) { 
	                for (OrderDetail detail : order.getOrderDetails()) {
	                    String productCode = detail.getProduct().getCode();
	                    int quantityOrdered = detail.getQuantity();
	                    int quantityInStock = productDAO.getStockQuantityByProductCode(productCode);
	                    productDAO.updateProductQuantity(productCode, quantityInStock - quantityOrdered);
	                }
	            }
	            
	            orderDAO.updateOrderStatus(orderId, status);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"))) {
            return "redirect:/admin/order?orderId=" + orderId;
        } else {
            return "redirect:/myOrder?orderId=" + orderId;
        }
        
    }
	
	@PostMapping({ "/admin/order/delete" })
	public String deleteOrder(@RequestParam("orderId") String orderId) {
		try {
	        this.orderDAO.delete(orderId);
	    } catch (Exception e) {
	    	
	    }
	    return "redirect:/admin/orderList";
	}
	
}
