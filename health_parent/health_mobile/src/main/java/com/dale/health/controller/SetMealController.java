package com.dale.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dale.health.constant.MessageConstant;
import com.dale.health.entity.Result;
import com.dale.health.pojo.Setmeal;
import com.dale.health.service.SetMealService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @RequestMapping("/getSetmeal.do")
    public Result getSetmeal() {
        try {
            List<Setmeal> allSetmeal = setMealService.getAllSetmeal();
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, allSetmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findById.do")
    public Result findSetmealDetailById(@Param("id") Integer id) {
        try {
            Setmeal setmeal = setMealService.getSetmealDetailById(id);
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

}
