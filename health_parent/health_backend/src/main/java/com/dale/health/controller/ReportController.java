package com.dale.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dale.health.constant.MessageConstant;
import com.dale.health.entity.Result;
import com.dale.health.service.MemberService;
import com.dale.health.service.ReportService;
import com.dale.health.service.SetMealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;
    @Reference
    private ReportService reportService;

    @RequestMapping("/getMemberReport.do")
    public Result getMemberReport() throws ParseException {
        Map<String, Object> map = new HashMap<>();

        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date time = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy-MM").format(time));
        }
        map.put("months", months);

        List<String> memberCount = new ArrayList<>();
        memberService.findMemberCountByMonths(months);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    @RequestMapping("/getSetmealReport.do")
    public Result getSetmealReport() {
        try {
            Map<String, Object> data = new HashMap<>();

            List<Map<String, Object>> setmealCount = setMealService.findSetmealCount();
            data.put("setmealCount", setmealCount);

            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");
                setmealNames.add(name);
            }
            data.put("setmealNames", setmealNames);
            return new Result(true, "套餐数据查询成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "套餐数据查询失败");
        }
    }

    @RequestMapping("/getBusinessReportData.do")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> data = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport.do")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            XSSFWorkbook excel = new XSSFWorkbook(Files.newInputStream(Paths.get(filePath)));

            XSSFSheet sheet = excel.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            excel.write(outputStream);

            outputStream.flush();
            outputStream.close();
            excel.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport4PDF.do")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jasper";
            // 将模板编译成二进制文件，便于填充数据
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
            // 填充数据，并将文件封装到jasperPrint中，便于输出
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, result, new JRBeanCollectionDataSource(hotSetmeal));

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");

            // 输出文件到输出流中，便于在网页上下载
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

//            outputStream.flush();
//            outputStream.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}
