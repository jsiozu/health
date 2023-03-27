package com.dale.health.service;

import com.dale.health.entity.PageResult;
import com.dale.health.entity.QueryPageBean;
import com.dale.health.pojo.CheckGroup;
import com.dale.health.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface CheckGroupService {

    public void addCheckgroup(CheckGroup checkGroup, List<Integer> ids);

    // public void matchCheckgroupAndCheckitemByIds(Integer checkgroupId, );

    public PageResult findPage(QueryPageBean queryPageBean);

    public Map<String, Object> selectCheckgroupById(Integer id);

    public void editCheckgroup(CheckGroup checkGroup, List<Integer> ids);

}
