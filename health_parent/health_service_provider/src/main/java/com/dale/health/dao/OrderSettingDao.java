package com.dale.health.dao;

import com.dale.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {

    public Integer countOrderByDate(@Param("date") String date);

    public Integer countOrderByDate2(@Param("date") String date);

    public void insertOrder(OrderSetting orderSetting);

    public void updateOrderByDate(@Param("date") String date, @Param("number") Integer number);

    public List<OrderSetting> getOrderSettingByMonth(@Param("year") String year, @Param("date") String date);

    public void updateNumberByDate(@Param("date") String date, @Param("number") Integer number);

    public OrderSetting selectByOrderDate(@Param("date") String time);

    public void editReservationsByOrderDate(OrderSetting orderSetting);


}
