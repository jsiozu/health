package com.dale.health.service;

import com.dale.health.entity.PageResult;
import com.dale.health.entity.QueryPageBean;
import com.dale.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    public void add(CheckItem checkItem);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void itemDeleteById(Integer id);

    public CheckItem findCheckitemById(Integer id);

    public void editCheckitem(CheckItem checkItem);

    public List<CheckItem> findAllCheckitem();
}
