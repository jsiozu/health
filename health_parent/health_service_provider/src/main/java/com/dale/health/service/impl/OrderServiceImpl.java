package com.dale.health.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.dale.health.dao.MemberDao;
import com.dale.health.dao.OrderDao;
import com.dale.health.dao.OrderSettingDao;
import com.dale.health.pojo.Member;
import com.dale.health.pojo.Order;
import com.dale.health.pojo.OrderSetting;
import com.dale.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public Integer checkIn(Map<String, Object> map) {
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = orderSettingDao.selectByOrderDate(orderDate);
        if (orderSetting == null) {
            throw new RuntimeException("当天不支持体检预约");
        }
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations > number) throw new RuntimeException("预约已满");

        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            Integer id = member.getId();
            Integer setmealId = (Integer) map.get("setmealId");
            Order order = new Order(id, orderDate, setmealId);
            List<Order> orders = orderDao.findByCondition(order);
            if (orders != null && orders.size() > 0) throw new RuntimeException("重复预约");
        } else {
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setName((String) map.get("name"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setMemberId(member.getId());
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERTYPE_WEIXIN);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.insertOrder(order);
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return order.getId();
    }

    @Override
    public Map<Object, Object> findById(Integer id) {
        Map byId4Detail = orderDao.findById4Detail(id);
        System.out.println(byId4Detail);
        return byId4Detail;
    }


}
