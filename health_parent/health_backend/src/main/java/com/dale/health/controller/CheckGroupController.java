package com.dale.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dale.health.constant.MessageConstant;
import com.dale.health.entity.PageResult;
import com.dale.health.entity.QueryPageBean;
import com.dale.health.entity.Result;
import com.dale.health.pojo.CheckGroup;
import com.dale.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 向t_checkgroup中添加检查项
     * @param map
     * @return
     */
    @RequestMapping("/add.do")
    public Result addCheckgroup(@RequestBody Map<String, String> map) {
        try {
            // 插入检查组
            CheckGroup checkgroup = JSON.parseObject(map.get("checkgroup"), CheckGroup.class);
            List<Integer> ids = JSON.parseObject(map.get("checkitemIds"), List.class);
            // 插入检查组相应检查项
            checkGroupService.addCheckgroup(checkgroup, ids);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页查询t_checkgroup表中数据
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.findPage(queryPageBean);
    }

    /**
     * 根据checkgroup的id查询checkgroup信息，以及关联的checkitem的id
     * @param groupId
     * @return
     */
    @GetMapping("/findById.do")
    public Result findById(@RequestParam("id") Integer groupId) {
        try {
            // 根据id在t_checkgroup表中搜索checkgroup，并根据id检索出匹配的检索项的信息放入到checkgroup对象中
            Map<String, Object> map = checkGroupService.selectCheckgroupById(groupId);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 修改checkgroup的信息，包括关联的checkitem信息
     * @param map
     * @return
     */
    @RequestMapping("/edit.do")
    public Result editCheckgroup(@RequestBody Map<String, String> map) {
        try {
            CheckGroup checkgroup = JSON.parseObject(map.get("checkgroup"), CheckGroup.class);
            List<Integer> ids = JSON.parseObject(map.get("checkitemIds"), List.class);
            checkGroupService.editCheckgroup(checkgroup, ids);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }

    }

}
