package com.dale.health.service;

import com.dale.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    public Integer checkIn(Map<String, Object> map);

    public Map<Object, Object> findById(Integer id);
}
