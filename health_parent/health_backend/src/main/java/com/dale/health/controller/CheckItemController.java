package com.dale.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dale.health.constant.MessageConstant;
import com.dale.health.entity.PageResult;
import com.dale.health.entity.QueryPageBean;
import com.dale.health.entity.Result;
import com.dale.health.pojo.CheckItem;
import com.dale.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference  // 查找dubbo注册的服务
    private CheckItemService checkItemService;

    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem) {

        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/pageQuery.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkItemService.pageQuery(queryPageBean);
    }

    @RequestMapping("itemDelete.do")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result itemDeleteById(@RequestBody CheckItem checkItem) {
        try {
            Integer id = checkItem.getId();
            checkItemService.itemDeleteById(id);
        } catch (Exception e) {
            e.printStackTrace();;
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @GetMapping("/findById.do")
    public Result findCheckitemById(@RequestParam("id") Integer id) {
        try{
            CheckItem checkitem = checkItemService.findCheckitemById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkitem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/edit.do")
    public Result editCheckitem(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.editCheckitem(checkItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @GetMapping("/findAll.do")
    public Result findAllCheckitem() {
        try {
            List<CheckItem> allCheckitem = checkItemService.findAllCheckitem();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, allCheckitem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

}
