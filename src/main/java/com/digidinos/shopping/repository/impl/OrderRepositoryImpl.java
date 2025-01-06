package com.digidinos.shopping.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.hibernate.query.Query;
import com.digidinos.shopping.entity.Order;
import com.digidinos.shopping.entity.OrderDetail;
import com.digidinos.shopping.entity.Product;
import com.digidinos.shopping.entity.User;
import com.digidinos.shopping.model.CartInfo;
import com.digidinos.shopping.model.CartLineInfo;
import com.digidinos.shopping.model.CustomerInfo;
import com.digidinos.shopping.model.OrderDetailInfo;
import com.digidinos.shopping.model.OrderInfo;
import com.digidinos.shopping.pagination.PaginationResult;
import com.digidinos.shopping.repository.OrderRepositoryCustom;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int getMaxOrderNum() {
        String sql = "SELECT COALESCE(MAX(o.orderNum), 0) FROM Order o";
        Integer maxOrderNum = (Integer) entityManager.createQuery(sql).getSingleResult();
        return maxOrderNum != null ? maxOrderNum : 0;
    }

    @Override
    public void saveOrder(CartInfo cartInfo, User user) {
        int orderNum = getMaxOrderNum() + 1; 
        Order order = new Order();
        
        order.setId(UUID.randomUUID().toString());
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());
        order.setStatus("Chưa xác nhận");

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setCustomerName(customerInfo.getName());
        order.setCustomerAddress(customerInfo.getAddress());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerPhone(customerInfo.getPhone());

        if (user != null) {
            order.setUser(user);
        } 
        
        entityManager.persist(order);

        List<CartLineInfo> lines = cartInfo.getCartLines();
        for (CartLineInfo line : lines) {
            OrderDetail detail = new OrderDetail();
            detail.setId(UUID.randomUUID().toString());
            detail.setOrder(order);
            detail.setAmount(line.getAmount());
            detail.setPrice(line.getProductInfo().getPrice());
            detail.setQuantity(line.getQuantity());
            // Gán sản phẩm vào chi tiết đơn hàng
            Product product = entityManager.find(Product.class, line.getProductInfo().getCode());
            detail.setProduct(product);
            
            entityManager.persist(detail);
        }

        entityManager.flush();
    }
    
    @Override
    public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
    	String sql = "Select new " + OrderInfo.class.getName()//
				+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, ord.status, "
				+ " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
				+ Order.class.getName() + " ord "//
				+ " order by ord.orderNum desc";

    	TypedQuery<OrderInfo> query = entityManager.createQuery(sql, OrderInfo.class);
    	
    	query.setFirstResult((page - 1) * maxResult); // set start index
        query.setMaxResults(maxResult);
        
        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
    }


    @Override
    public OrderInfo getOrderInfo(String orderId) {
        String sql = "SELECT new com.digidinos.shopping.model.OrderInfo(o.id, o.orderDate, o.orderNum, o.amount, o.status, " +
                     "o.user.id, o.customerName, o.customerAddress, o.customerEmail, o.customerPhone) FROM Order o WHERE o.id = :orderId";
        return entityManager.createQuery(sql, OrderInfo.class)
                            .setParameter("orderId", orderId)
                            .getSingleResult();
    }

    @Override
    public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
    	String sql = "Select new " + OrderDetailInfo.class.getName() //
				+ "(d.id, d.product.code, d.product.name , d.quantity,d.price,d.amount) "//
				+ " from " + OrderDetail.class.getName() + " d "//
				+ " where d.order.id = :orderId ";

    	TypedQuery<OrderDetailInfo> query = entityManager.createQuery(sql, OrderDetailInfo.class);
        query.setParameter("orderId", orderId);

        return query.getResultList();
    }
    
    @Override
    public List<OrderInfo> listOrdersByUserId(Integer userId) {
        String sql = "SELECT new com.digidinos.shopping.model.OrderInfo(o.id, o.orderDate, o.orderNum, o.amount, o.status, " +
                     "o.user.id, o.customerName, o.customerAddress, o.customerEmail, o.customerPhone) FROM Order o WHERE o.user.id = :userId ORDER BY o.orderNum DESC";
        return entityManager.createQuery(sql, OrderInfo.class)
                            .setParameter("userId", userId)
                            .getResultList();
    }

    @Override
    public PaginationResult<OrderInfo> listOrderByUser(Integer userId, int page, int maxResult, int maxNavigationPage) {
        String sql = "SELECT new com.digidinos.shopping.model.OrderInfo(o.id, o.orderDate, o.customerName, " +
                     "o.customerAddress, o.customerEmail, o.amount, o.status) " +
                     "FROM Order o WHERE o.user.id = :userId ORDER BY o.orderDate DESC";

        Query<OrderInfo> query = (Query<OrderInfo>) entityManager.createQuery(sql, OrderInfo.class)
                                              .setParameter("userId", userId);
        query.setFirstResult((page - 1) * maxResult);
        query.setMaxResults(maxResult);

        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
    }
    
    @Override
    public String getOrderStatus(String orderId) {
        String sql = "SELECT o.status FROM Order o WHERE o.id = :orderId";
        return entityManager.createQuery(sql, String.class)
                            .setParameter("orderId", orderId)
                            .getSingleResult();
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        String sql = "UPDATE Order o SET o.status = :status WHERE o.id = :orderId";
        entityManager.createQuery(sql)
                     .setParameter("status", status)
                     .setParameter("orderId", orderId)
                     .executeUpdate();
    }
    
}