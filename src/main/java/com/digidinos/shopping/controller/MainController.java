package com.digidinos.shopping.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digidinos.shopping.dao.OrderDAO;
import com.digidinos.shopping.dao.ProductDAO;
import com.digidinos.shopping.entity.Product;
import com.digidinos.shopping.entity.User;
import com.digidinos.shopping.form.CustomerForm;
import com.digidinos.shopping.model.CartInfo;
import com.digidinos.shopping.model.CustomerInfo;
import com.digidinos.shopping.model.OrderDetailInfo;
import com.digidinos.shopping.model.OrderInfo;
import com.digidinos.shopping.model.ProductInfo;
import com.digidinos.shopping.pagination.PaginationResult;
import com.digidinos.shopping.service.OrderService;
import com.digidinos.shopping.service.UserServiceImpl;
import com.digidinos.shopping.utils.Utils;
import com.digidinos.shopping.validator.CartFormValidator;
import com.digidinos.shopping.validator.CustomerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Transactional
public class MainController {

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private CustomerFormValidator customerFormValidator;
	
	@Autowired
	private CartFormValidator cartFormValidator;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private OrderService orderService;

	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		// Trường hợp update SL trên giỏ hàng.
		// (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
		if (target.getClass() == CartInfo.class) {
			dataBinder.setValidator(cartFormValidator);
		}

		// Trường hợp save thông tin khách hàng.
		// (@ModelAttribute @Validated CustomerInfo customerForm)
		else if (target.getClass() == CustomerForm.class) {
			dataBinder.setValidator(customerFormValidator);
		}

	}

	@GetMapping("/403")
	public String accessDenied() {
		return "/403";
	}

	@GetMapping("/")
	public String home() {
		return "index";
	}

	// Danh sách sản phẩm.
	@GetMapping({ "/productList" })
	public String listProductHandler(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		final int maxResult = 5;
		final int maxNavigationPage = 10;

		PaginationResult<ProductInfo> result = productDAO.queryProducts(page, //
				maxResult, maxNavigationPage, likeName);

		model.addAttribute("paginationProducts", result);
		return "productList";
	}

	// API trả về danh sách sản phẩm ở định dạng JSON
	@RestController
	@RequestMapping("/api/products")
	public class ProductApiController {

	    @Autowired
	    private ProductDAO productDAO;

	    @GetMapping
	    public PaginationResult<ProductInfo> getProducts(
	            @RequestParam(value = "name", defaultValue = "") String likeName,
	            @RequestParam(value = "page", defaultValue = "1") int page) {

	        final int maxResult = 8;
	        final int maxNavigationPage = 10;

	        // Lấy danh sách sản phẩm từ DAO
	        PaginationResult<ProductInfo> result = productDAO.queryProducts(page, maxResult, maxNavigationPage, likeName);
	        
	        // Chuyển đổi hình ảnh thành Base64
	        for (ProductInfo product : result.getList()) {
	            if (product.getImage() != null && product.getImage().length > 0) {
	                String base64Image = Base64.getEncoder().encodeToString(product.getImage());
	                product.setImageBase64(base64Image);  
	            }
	        }

	        return result;
	    }
	}

	// GET: Thêm sản phẩm vào giỏ hàng theo mã (code)
	@GetMapping({ "/buyProduct" })
	public String listProductHandler(HttpServletRequest request, Model model, //
			@RequestParam(value = "code", defaultValue = "") String code) {

		Product product = null;
		if (code != null && code.length() > 0) {
			product = productDAO.findProduct(code);
		}
		if (product != null) {

			// 
			CartInfo cartInfo = Utils.getCartInSession(request);

			ProductInfo productInfo = new ProductInfo(product);

			cartInfo.addProduct(productInfo, 1);
		}

		return "redirect:/shoppingCart";
	}

	// GET: Xóa sản phẩm khỏi giỏ hàng theo mã (code)
	@GetMapping({ "/shoppingCartRemoveProduct" })
	public String removeProductHandler(HttpServletRequest request, Model model, //
			@RequestParam(value = "code", defaultValue = "") String code) {
		Product product = null;
		if (code != null && code.length() > 0) {
			product = productDAO.findProduct(code);
		}
		if (product != null) {

			CartInfo cartInfo = Utils.getCartInSession(request);

			ProductInfo productInfo = new ProductInfo(product);

			cartInfo.removeProduct(productInfo);

		}

		return "redirect:/shoppingCart";
	}

	// POST: Cập nhập số lượng cho các sản phẩm đã mua.
	@PostMapping({ "/shoppingCart" })
	public String shoppingCartUpdateQty(HttpServletRequest request, //
			Model model, //
			@ModelAttribute("cartForm") @Validated CartInfo cartForm,
			BindingResult result) {

		if (result.hasErrors()) {
	        return "shoppingCart";
	    }
		
		CartInfo cartInfo = Utils.getCartInSession(request);
		cartInfo.updateQuantity(cartForm);

		return "redirect:/shoppingCart";
	}

	// GET: Hiển thị giỏ hàng.
	@GetMapping({ "/shoppingCart" })
	public String shoppingCartHandler(HttpServletRequest request, Model model) {
		CartInfo myCart = Utils.getCartInSession(request);

		model.addAttribute("cartForm", myCart);
		return "shoppingCart";
	}

	// GET: Nhập thông tin khách hàng.
	@GetMapping({ "/shoppingCartCustomer" })
	public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {

		CartInfo cartInfo = Utils.getCartInSession(request);

		if (cartInfo.isEmpty()) {

			return "redirect:/shoppingCart";
		}
		CustomerInfo customerInfo = cartInfo.getCustomerInfo();

		CustomerForm customerForm = new CustomerForm(customerInfo);

		model.addAttribute("customerForm", customerForm);

		return "shoppingCartCustomer";
	}

	// POST: Save thông tin khách hàng.
	@PostMapping({ "/shoppingCartCustomer" })
	public String shoppingCartCustomerSave(HttpServletRequest request, //
			Model model, //
			@ModelAttribute("customerForm") @Validated CustomerForm customerForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			customerForm.setValid(false);
			// Forward tới trang nhập lại.
			return "shoppingCartCustomer";
		}

		customerForm.setValid(true);
		CartInfo cartInfo = Utils.getCartInSession(request);
		CustomerInfo customerInfo = new CustomerInfo(customerForm);
		cartInfo.setCustomerInfo(customerInfo);

		return "redirect:/shoppingCartConfirmation";
	}

	// GET: Xem lại thông tin để xác nhận.
	@GetMapping({ "/shoppingCartConfirmation" })
	public String shoppingCartConfirmationReview(HttpServletRequest request, Model model, Principal principal) {
		CartInfo cartInfo = Utils.getCartInSession(request);

	    if (cartInfo == null || cartInfo.isEmpty()) {
	        return "redirect:/shoppingCart";
	    } 

	    if (principal != null && !cartInfo.isValidCustomer()) {
	        String username = principal.getName();
	        User user = userService.findUserByUserName(username);

	        CustomerInfo customerInfo = new CustomerInfo();
	        customerInfo.setName(user.getFullName());
	        customerInfo.setEmail(user.getEmail());
	        customerInfo.setPhone(user.getPhone());
	        customerInfo.setAddress(user.getAddress());

	        cartInfo.setCustomerInfo(customerInfo);
	    }

	    if (!cartInfo.isValidCustomer()) {
	        return "redirect:/shoppingCartCustomer";
	    }

	    model.addAttribute("myCart", cartInfo);
	    return "shoppingCartConfirmation";
	}

	// POST: Gửi đơn hàng (Save).
	@PostMapping({ "/shoppingCartConfirmation" })
	public String shoppingCartConfirmationSave(HttpServletRequest request, Model model, Principal principal) {
		CartInfo cartInfo = Utils.getCartInSession(request);

	    if (cartInfo == null || cartInfo.isEmpty()) {
	        return "redirect:/shoppingCart";
	    }

	    User user = null;
	    if (principal != null && !cartInfo.isValidCustomer()) {
	        String username = principal.getName();
	        user = userService.findUserByUserName(username);
	        
	        CustomerInfo customerInfo = new CustomerInfo();
	        customerInfo.setName(user.getFullName());
	        customerInfo.setEmail(user.getEmail());
	        customerInfo.setPhone(user.getPhone());
	        customerInfo.setAddress(user.getAddress());

	        cartInfo.setCustomerInfo(customerInfo);
	    }

	    if (!cartInfo.isValidCustomer()) {
	        return "redirect:/shoppingCartCustomer";
	    }

	    try {
	        orderDAO.saveOrder(cartInfo, user);
	    } catch (Exception e) {
	        model.addAttribute("errorMessage", "Error occurred while saving your order. Please try again.");
	        return "shoppingCartConfirmation";
	    }

	    // Xóa giỏ hàng khỏi session.
	    Utils.removeCartInSession(request);

	    // Lưu thông tin đơn hàng cuối đã xác nhận mua.
	    Utils.storeLastOrderedCartInSession(request, cartInfo);

	    return "redirect:/shoppingCartFinalize";
	}

	// GET: Thông tin đơn hàng cuối cùng 
	@GetMapping({ "/shoppingCartFinalize" })
	public String shoppingCartFinalize(HttpServletRequest request, Model model) {

		CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);

		if (lastOrderedCart == null) {
			return "redirect:/shoppingCart";
		}
		model.addAttribute("lastOrderedCart", lastOrderedCart);
		return "shoppingCartFinalize";
	}
	
	@GetMapping({ "/myOrderList" })
	public String listUserOrders(@RequestParam(defaultValue = "1") int page,
	                              @RequestParam(defaultValue = "5") int maxResult,
	                              @RequestParam(defaultValue = "5") int maxNavigationPage,
	                              Principal principal,
	                              Model model) {
	    String username = principal.getName();
	    User user = userService.findUserByUserName(username); 

	    PaginationResult<OrderInfo> paginationResult = orderService.getOrdersByUserId(user.getId(), page, maxResult, maxNavigationPage);
	    model.addAttribute("paginationResult", paginationResult);
	    return "myOrderList";
	}
	
	@GetMapping({ "/myOrder" })
	public String viewOrderDetails(@RequestParam("orderId") String orderId, Principal principal, Model model) {
	    String username = principal.getName();
	    User user = userService.findUserByUserName(username); 

	    OrderInfo orderInfo = orderService.getOrderById(orderId);

	    if (orderInfo == null || !orderInfo.getUserId().equals(user.getId())) {
	        return "redirect:/myOrderList"; 
	    }


	    List<OrderDetailInfo> details = orderService.getOrderDetails(orderId);
	    orderInfo.setDetails(details);

	    String orderStatus = orderService.getOrderStatus(orderId);

	    model.addAttribute("orderInfo", orderInfo);
	    model.addAttribute("orderStatus", orderStatus);

	    return "myOrder"; 
	}

	// GET: Hiển thị hình ảnh sản phẩm theo mã (code)
	@GetMapping({ "/productImage" })
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("code") String code) throws IOException {
		Product product = null;
		if (code != null) {
			product = this.productDAO.findProduct(code);
		}
		if (product != null && product.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(product.getImage());
		}
		response.getOutputStream().close();
	}

}