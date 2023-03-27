package com.dale.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dale.health.dao.MemberDao;
import com.dale.health.dao.OrderDao;
import com.dale.health.service.ReportService;
import com.dale.health.utils.DateUtils;
import com.dale.health.utils.TimeFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        //报表日期
        String today = TimeFormatUtils.getFormatTime(new Date());

        // 本日新增会员数
        Integer memberDaoMemberCountByDate = memberDao.findMemberCountByDate(today);

        // 总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        // 本周新增会员数
        String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);

        // 本月新增会员数
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);

        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(monday);

        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //热门套餐（取前4）
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        Map<String, Object> result = new HashMap<>();
        result.put("reportDate", today);
        result.put("todayNewMember", memberDaoMemberCountByDate);
        result.put("totalMember", totalMember);
        result.put("thisWeekNewMember", thisWeekNewMember);
        result.put("thisMonthNewMember", thisMonthNewMember);
        result.put("todayOrderNumber", todayOrderNumber);
        result.put("thisWeekOrderNumber", thisWeekOrderNumber);
        result.put("thisMonthOrderNumber", thisMonthOrderNumber);
        result.put("todayVisitsNumber", todayVisitsNumber);
        result.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        result.put("hotSetmeal", hotSetmeal);

        return result;
    }


}
