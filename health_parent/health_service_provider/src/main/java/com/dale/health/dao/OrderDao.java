package com.dale.health.dao;

import com.dale.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    public void insertOrder(Order order);
    public List<Order> findByCondition(Order order);
    public Order selectById(@Param("id") Integer id);
    public Map findById4Detail(@Param("id") Integer id);
    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);
    public List<Map> findHotSetmeal();
}
