package com.dale.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dale.health.constant.MessageConstant;
import com.dale.health.entity.Result;
import com.dale.health.pojo.OrderSetting;
import com.dale.health.service.OrderSettingService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.dale.health.utils.POIUtils.*;


@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload.do")
    public Result uploadExcel(@RequestParam("excelFile") MultipartFile excelFile) throws IOException {
        try {
            List<String[]> datas = readExcel(excelFile);
            List<OrderSetting> orderSettings = new ArrayList<>();
            datas.forEach(d -> {
                String orderDate = d[0];
                String number = d[1];
                OrderSetting orderSetting = new OrderSetting(new Date(orderDate), Integer.parseInt(number));
                System.out.println(orderSetting);
                System.out.println(orderSetting.getOrderDate().toString());
                orderSettings.add(orderSetting);
            });
            orderSettingService.registerOrder(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth.do")
    public Result getOrderSettingByMonth(String date) {
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate.do")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/testTimeUtils.do")
    public void test() {
        OrderSetting orderSetting = new OrderSetting();
        orderSetting.setOrderDate(new Date());
        orderSettingService.testTimeFormatUtils(orderSetting);
    }


}
