package com.dale.health.dao;

import com.dale.health.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface CheckItemDao {

    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(@Param("value") String value);

    public void deleteCheckitemById(@Param("id") Integer id);

    public long countCheckitemById(@Param("id") Integer id);

    public CheckItem selectCheckitemById(@Param("id") Integer id);

    public void updateCheckitem(CheckItem checkItem);

    public List<CheckItem> selectAllCheckitem();

    public List<CheckItem> selectDetailByCheckgroupId(@Param("id") Integer id);

}
