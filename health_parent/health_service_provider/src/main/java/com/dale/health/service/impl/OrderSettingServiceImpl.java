package com.dale.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dale.health.dao.OrderSettingDao;
import com.dale.health.pojo.OrderSetting;
import com.dale.health.service.OrderSettingService;
import com.dale.health.utils.TimeFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void registerOrder(List<OrderSetting> orders) {
            orders.forEach(order -> {
                String formatTime = TimeFormatUtils.getFormatTime(order.getOrderDate());
                Integer num = orderSettingDao.countOrderByDate(formatTime);
                if (num != null && num <= 0) {
                    orderSettingDao.insertOrder(order);
                } else {
                    orderSettingDao.updateOrderByDate(formatTime, order.getNumber());
                }
            });
    }

    @Override
    public List<Map> getOrderSettingByMonth(String time) {
        String year = time.split("-")[0];
        String date = time.split("-")[1];
        List<OrderSetting> orderSettingByMonth = orderSettingDao.getOrderSettingByMonth(year, date);
        List<Map> list = new ArrayList<>();
        orderSettingByMonth.forEach(orderSetting -> {
            Map<String, Integer> map = new HashMap<>();
            map.put("date", orderSetting.getOrderDate().getDate());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            list.add(map);
        });
        return list;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        String formatTime = TimeFormatUtils.getFormatTime(orderSetting.getOrderDate());
        Integer num = orderSettingDao.countOrderByDate(formatTime);
        if (num != null && num <= 0) {
            orderSettingDao.insertOrder(orderSetting);
        } else {
            orderSettingDao.updateNumberByDate(formatTime, orderSetting.getNumber());
        }
    }

    @Override
    public void testTimeFormatUtils(OrderSetting orderSetting) {
        String formatTime = TimeFormatUtils.getFormatTime(orderSetting.getOrderDate());
        System.out.println(formatTime);
        Integer integer = orderSettingDao.countOrderByDate2(formatTime);
    }


}
