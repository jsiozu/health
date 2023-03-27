package com.dale.health.dao;

import com.dale.health.pojo.CheckGroup;
import com.dale.health.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetMealDao {

    public Page<CheckGroup> findPage(@Param("queryString") String queryString);

    public void editSetmeal(Setmeal setmeal);

    public void deleteCheckgroupBySetmealId(@Param("id") Integer id);

    public void matchCheckgroupWithSetmeal(@Param("setmealId") Integer id,
                                           @Param("checkgroupIds")List<Integer> ids);

    public void addSetmeal(Setmeal setmeal);

    public Long countSetmealById(@Param("id") Integer id);

    public Setmeal selectById(@Param("id") Integer id);

    public List<Setmeal> selectAllSetmeal();

    public Setmeal selectDetailById(@Param("id") Integer id);

    public List<Map<String, Object>> selectSetmealCount();

}
