package com.dale.health.dao;


import com.dale.health.pojo.CheckGroup;
import com.dale.health.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {

    public void addCheckgroup(CheckGroup checkGroup);

    public void matchCheckgroupAndCheckitemByIds(@Param("checkgroupId") Integer checkgroupId,
                                                 @Param("ids") List<Integer> ids);

    public Long selectCheckgroupByCode(@Param("code") String code);

    public Page<CheckGroup> findPage(@Param("queryString") String queryString);

    public CheckGroup selectById(@Param("id") Integer id);

    public List<Integer> selectCheckitemByCheckgroupId(@Param("id") Integer groupId);
//    checkGroupDao.editCheckgroup(checkGroup);
    public void editCheckgroup(CheckGroup checkGroup);

    public void deleteCheckitemidsByGroupId(@Param("id") Integer groupId);

    public List<CheckGroup> selectAllCheckgroup();

    public List<Integer> selectCheckgroupBySetmealId(@Param("id") Integer id);

    public List<CheckGroup> selectDetailBySetmealId(@Param("id") Integer id);


}
