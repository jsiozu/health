package com.dale.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dale.health.dao.CheckItemDao;
import com.dale.health.entity.PageResult;
import com.dale.health.entity.QueryPageBean;
import com.dale.health.pojo.CheckItem;
import com.dale.health.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {

        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> checkItems = checkItemDao.selectByCondition(queryString);

        long total = checkItems.getTotal();
        List<CheckItem> result = checkItems.getResult();

        return new PageResult(total, result);
    }

    @Override
    public void itemDeleteById(Integer id) {
        long l = checkItemDao.countCheckitemById(id);
        if (l > 0) {
            throw new RuntimeException();
        } else {
            checkItemDao.deleteCheckitemById(id);
        }
    }

    @Override
    public CheckItem findCheckitemById(Integer id) {
        CheckItem checkItem = checkItemDao.selectCheckitemById(id);
        if (checkItem == null) {
            throw new RuntimeException();
        }
        return checkItem;
    }

    @Override
    public void editCheckitem(CheckItem checkItem) {
        checkItemDao.updateCheckitem(checkItem);
    }

    @Override
    public List<CheckItem> findAllCheckitem() {
        return checkItemDao.selectAllCheckitem();
    }

}
