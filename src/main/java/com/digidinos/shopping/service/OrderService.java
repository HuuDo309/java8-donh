package com.digidinos.shopping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digidinos.shopping.dao.OrderDAO;
import com.digidinos.shopping.entity.Order;
import com.digidinos.shopping.entity.User;
import com.digidinos.shopping.model.CartInfo;
import com.digidinos.shopping.model.OrderDetailInfo;
import com.digidinos.shopping.model.OrderInfo;
import com.digidinos.shopping.pagination.PaginationResult;

@Service
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;

    // Lấy danh sách order với phân trang
    public PaginationResult<OrderInfo> getOrders(int page, int maxResult, int maxNavigationPage) {
        return orderDAO.listOrderInfo(page, maxResult, maxNavigationPage);
    }

    public PaginationResult<OrderInfo> getOrdersByUserId(Integer userId, int page, int maxResult, int maxNavigationPage) {
        return orderDAO.listOrderByUser(userId, page, maxResult, maxNavigationPage);
    }
    
    // Tìm kiếm order bằng ID
    public OrderInfo getOrderById(String orderId) {
        return orderDAO.getOrderInfo(orderId);
    }

    // Lấy trạng thái order
    public String getOrderStatus(String orderId) {
        return orderDAO.getOrderStatus(orderId);
    }

    // Lấy chi tiết order
    public List<OrderDetailInfo> getOrderDetails(String orderId) {
        return orderDAO.listOrderDetailInfos(orderId);
    }

    // Cập nhật trạng thái order
    public void updateOrderStatus(String orderId, String newStatus) {
        Order order = orderDAO.findOrder(orderId);
        if (order != null) {
            order.setStatus(newStatus);
        }
    }

    // Xóa order
    @Transactional
    public void deleteOrder(String orderId) {
        orderDAO.delete(orderId);
    }

    // Lưu đơn hàng (thêm mới)
    @Transactional
    public void saveOrder(CartInfo cartInfo, User user) {
        orderDAO.saveOrder(cartInfo, user);
    }
}
