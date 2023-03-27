package com.dale.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dale.health.dao.CheckGroupDao;
import com.dale.health.entity.PageResult;
import com.dale.health.entity.QueryPageBean;
import com.dale.health.pojo.CheckGroup;
import com.dale.health.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public void addCheckgroup(CheckGroup checkGroup, List<Integer> ids) {
        Long num = checkGroupDao.selectCheckgroupByCode(checkGroup.getCode());
        if (num > 0) {
            throw new RuntimeException();
        } else {
            checkGroupDao.addCheckgroup(checkGroup);
            Integer id = checkGroup.getId();
            if (ids != null && ids.size() > 0) {
                checkGroupDao.matchCheckgroupAndCheckitemByIds(id, ids);
            } else {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> checkGroups = checkGroupDao.findPage(queryString);

        long total = checkGroups.getTotal();
        List<CheckGroup> result = checkGroups.getResult();

        return new PageResult(total, result);
    }

    @Override
    public Map<String, Object> selectCheckgroupById(Integer id) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            CheckGroup checkGroup = checkGroupDao.selectById(id);
            map.put("checkgroup", checkGroup);
            if (checkGroup != null) {
                List<Integer> checkItems = checkGroupDao.selectCheckitemByCheckgroupId(id);
                map.put("checkitemids", checkItems);
                return map;
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void editCheckgroup(CheckGroup checkGroup, List<Integer> ids) {
        try {
            checkGroupDao.editCheckgroup(checkGroup);
            Integer groupId = checkGroup.getId();
            checkGroupDao.deleteCheckitemidsByGroupId(groupId);
            if (ids != null && ids.size() > 0) {
                checkGroupDao.matchCheckgroupAndCheckitemByIds(groupId, ids);
            } else {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
