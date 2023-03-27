package com.dale.health.service;

import com.dale.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    public void registerOrder(List<OrderSetting> orders);

    public List<Map> getOrderSettingByMonth(String date);

    public void editNumberByDate(OrderSetting orderSetting);

    void testTimeFormatUtils(OrderSetting orderSetting);
}
