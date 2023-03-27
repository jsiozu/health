package com.dale.health.service;

import com.dale.health.entity.PageResult;
import com.dale.health.entity.QueryPageBean;
import com.dale.health.pojo.CheckGroup;
import com.dale.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SetMealService {

    public List<CheckGroup> getAllCheckgroup();

    public PageResult findPage(QueryPageBean queryPageBean);

    public void addSetmeal(Setmeal setmeal, List<Integer> ids);

    public Map<String, Object> selectById(Integer id);

    public void editSetmeal(Setmeal setmeal, List<Integer> ids);

    public List<Setmeal> getAllSetmeal();

    public Setmeal findSetmealDetailById(Integer id);

    public Setmeal getSetmealDetailById(Integer id);

    public List<Map<String, Object>> findSetmealCount();

}
