package com.digidinos.shopping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digidinos.shopping.entity.Order;
import com.digidinos.shopping.entity.User;
import com.digidinos.shopping.model.CartInfo;
import com.digidinos.shopping.model.OrderDetailInfo;
import com.digidinos.shopping.model.OrderInfo;
import com.digidinos.shopping.pagination.PaginationResult;
import com.digidinos.shopping.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderREPO;

    // Lấy danh sách order với phân trang
    public PaginationResult<OrderInfo> getOrders(int page, int maxResult, int maxNavigationPage) {
        return orderREPO.listOrderInfo(page, maxResult, maxNavigationPage);
    }

    public PaginationResult<OrderInfo> getOrdersByUserId(Integer userId, int page, int maxResult, int maxNavigationPage) {
        return orderREPO.listOrderByUser(userId, page, maxResult, maxNavigationPage);
    }
    
    // Tìm kiếm order bằng ID
    public OrderInfo getOrderById(String orderId) {
        return orderREPO.getOrderInfo(orderId);
    }

    // Lấy trạng thái order
    public String getOrderStatus(String orderId) {
        return orderREPO.getOrderStatus(orderId);
    }

    // Lấy chi tiết order
    public List<OrderDetailInfo> getOrderDetails(String orderId) {
        return orderREPO.listOrderDetailInfos(orderId);
    }

    // Cập nhật trạng thái order
    public void updateOrderStatus(String orderId, String newStatus) {
        Order order = orderREPO.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(newStatus);
        }
    }

    // Xóa order
    @Transactional
    public void deleteOrder(String orderId) {
    	orderREPO.deleteById(orderId);
    }

    // Lưu đơn hàng (thêm mới)
    @Transactional
    public void saveOrder(CartInfo cartInfo, User user) {
    	orderREPO.saveOrder(cartInfo, user);
    }
}
