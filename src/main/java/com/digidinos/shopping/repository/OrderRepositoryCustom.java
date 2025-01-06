package com.digidinos.shopping.repository;

import java.util.List;

import com.digidinos.shopping.entity.User;
import com.digidinos.shopping.model.CartInfo;
import com.digidinos.shopping.model.OrderDetailInfo;
import com.digidinos.shopping.model.OrderInfo;
import com.digidinos.shopping.pagination.PaginationResult;

public interface OrderRepositoryCustom {
	int getMaxOrderNum();
    PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage);
    OrderInfo getOrderInfo(String orderId);
    List<OrderDetailInfo> listOrderDetailInfos(String orderId);
    List<OrderInfo> listOrdersByUserId(Integer userId);
    PaginationResult<OrderInfo> listOrderByUser(Integer userId, int page, int maxResult, int maxNavigationPage);
    String getOrderStatus(String orderId);
    void updateOrderStatus(String orderId, String status);
    void saveOrder(CartInfo cartInfo, User user);
    
}
