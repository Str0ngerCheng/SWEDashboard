package com.bio.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.bio.common.service.ContextService;
import com.bio.common.utils.ExcelUtils;
import com.bio.common.utils.Result;
import com.bio.common.utils.excel.ExcelFormat;
import com.bio.common.utils.excel.ExcelHeaderInfo;
import com.bio.sys.domain.*;
import com.bio.sys.service.QueryService;
import com.bio.sys.service.ReportContentService;
import com.bio.sys.service.ReportService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/bio/query")
public class QueryController {
    @Autowired
    private ContextService contextService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private QueryService queryService;


    @Autowired
    private ReportContentService reportContentService;

    private static List<QueryDO> reportExcelSummary;


    @GetMapping()
    @RequiresPermissions("bio:query:query")
    public String Query() {
        return "bio/query/query";
    }

    @GetMapping("/userSelect")
    @RequiresPermissions("bio:query:query")
    public String userSelect() {
        return "bio/query/userSelect";
    }


    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("bio:report:report")
    public Result<Page<QueryDO>> list(Integer pageNumber, Integer pageSize,ReportDO reportDTO) {
        UserDO userDO = contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        List<ReportDO> mylist=new ArrayList<>();
        if (userDO.getroleId().intValue() == 1 || userDO.getroleId().intValue() == 4 ) { // 超级管理员，默认所有的报告
            //TODO: 不做处理
           mylist = reportService.getReportsAll();
        }

        if (userDO.getroleId().intValue() == 2 || userDO.getroleId().intValue() == 3) { // 专题组长，只显示负责的学生 的报告
              String deptName=userDO.getDeptName();
              mylist=reportService.getReportsByDepName(deptName);
        }

        List<QueryDO> querylist = new ArrayList<>();
        Integer id=1;
        for (ReportDO products : mylist) {
            String contentId = products.getContentId();
            ReportContentDO reportContentDO = reportContentService.getByUUID(contentId);
            querylist.add(new QueryDO(id++,products.getTitle(), reportContentDO.getSummary(), reportContentDO.getNextPlan()));
        }
        reportExcelSummary=querylist;
        Page page1 = new Page(pageNumber, pageSize);
        page1.setTotal(querylist.size());
        int startIndex=(pageNumber-1)*pageSize;
        int endIndex=Math.min(startIndex+pageSize,querylist.size());
        page1.setRecords(querylist.subList(startIndex,endIndex));
        return Result.ok(page1);
    }

    @ResponseBody
    @GetMapping("/queryReport")
    @RequiresPermissions("bio:report:report")
    public Result<Page<QueryDO>> queryReport(Integer pageNumber, Integer pageSize, String userName, String datetimepicker1, String datetimepicker2, Integer WeekMonth) throws ParseException {
        List<QueryDO> querylist = new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate=sdf.parse(datetimepicker1);
        Date endDate=sdf.parse(datetimepicker2);
        List<ReportDO> myreport=new ArrayList<>();
        if (WeekMonth== 1) {
            myreport = reportService.getReportsQuery(startDate, endDate, 1);

        }else{
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(startDate);
            int month=calendar.get(calendar.MONTH)+1;
            myreport = reportService.getReportsByMonth(month,1);
        }
        Integer id=1;
        for (ReportDO products : myreport) {
            if (products.getAuthorName().equals(userName)  || userName.equals("") || userName.equals("超级管理员")) {
                String contentId = products.getContentId();
                ReportContentDO reportContentDO = reportContentService.getByUUID(contentId);
                querylist.add(new QueryDO(id++,products.getTitle(), reportContentDO.getSummary(), reportContentDO.getNextPlan()));
            }
        }
        reportExcelSummary=querylist;
        Page page1 = new Page(pageNumber, pageSize);
        page1.setTotal(querylist.size());
        int startIndex=(pageNumber-1)*pageSize;
        int endIndex=Math.min(startIndex+pageSize,querylist.size());
        page1.setRecords(querylist.subList(startIndex,endIndex));
        return Result.ok(page1);
    }


    @ResponseBody
    @GetMapping("/queryDepReport")
    @RequiresPermissions("bio:report:report")
    public Result<Page<QueryDO>> queryDepReport(Integer pageNumber, Integer pageSize, String topicName, String datetimepicker_t1, String datetimepicker_t2, Integer WeekMonth1) throws ParseException {
        List<QueryDO> querylist = new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate=sdf.parse(datetimepicker_t1);
        Date endDate=sdf.parse(datetimepicker_t2);
        List<ReportDO> myreport=new ArrayList<>();
        if (WeekMonth1== 1) {
            myreport = reportService.getReportsQuery(startDate, endDate, 1);
        }else{
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(startDate);
            int month=calendar.get(calendar.MONTH)+1;
            myreport = reportService.getReportsByMonth(month,1);
        }
        Integer id=1;
        for (ReportDO products : myreport) {
            if (products.getDeptName().equals(topicName)  || topicName.equals("智慧地球")) {
                String contentId = products.getContentId();
                ReportContentDO reportContentDO = reportContentService.getByUUID(contentId);
                querylist.add(new QueryDO(id++,products.getTitle(), reportContentDO.getSummary(), reportContentDO.getNextPlan()));
            }
        }
        reportExcelSummary=querylist;
        Page page1 = new Page(pageNumber, pageSize);
        page1.setTotal(querylist.size());
        int startIndex=(pageNumber-1)*pageSize;
        int endIndex=Math.min(startIndex+pageSize,querylist.size());
        page1.setRecords(querylist.subList(startIndex,endIndex));
        return Result.ok(page1);
    }

    @ResponseBody
    @GetMapping("/searchKey")
    @RequiresPermissions("bio:report:report")
    public Result<Page<ReportContentDO>> findByPageNo(Integer pageNumber, Integer pageSize, String searchKey){
        List<ReportContentDO> mylist=reportContentService.getSearchKey(searchKey);
        List<QueryDO> querylist = new ArrayList<>();
        Integer id=1;
        for(ReportContentDO reportContentDO:mylist){
            ReportDO myreport= reportService.getReportsByContentId(reportContentDO.getUuid());
            querylist.add(new QueryDO(id++,myreport.getTitle(),reportContentDO.getSummary(),reportContentDO.getNextPlan()));
        }
        reportExcelSummary=querylist;
        Page page1 = new Page(pageNumber, pageSize);
        page1.setTotal(querylist.size());
        int startIndex=(pageNumber-1)*pageSize;
        int endIndex=Math.min(startIndex+pageSize,querylist.size());
        page1.setRecords(querylist.subList(startIndex,endIndex));
        return Result.ok(page1);
    }

    @ResponseBody
    @GetMapping("/batchExport")
    @RequiresPermissions("bio:report:report")
    public void BatchExport(@RequestParam(value="ids") String ids, HttpServletResponse response) {
        String filename= null;
        try {
            filename = URLEncoder.encode("汇总表","UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<TopicDao> topics= new ArrayList<>();
        String[] myids=ids.split(",");
        List<String> mylist=Arrays.asList(myids);
        for(QueryDO products: reportExcelSummary){
           if(mylist.contains(products.getId().toString())) {
               ReportDO reportDO=reportService.getReportsByTitle(products.getTitle());
               ReportContentDO reportContentDO=reportContentService.getByUUID(reportDO.getContentId());
               topics.add(new TopicDao(reportDO.getDeptName(),reportDO.getAuthorName(),reportDO.getTitle(),products.getSummary(),reportContentDO.getProblem(),products.getPlan()));
           }
        }
        ExcelUtils excelUtils = new ExcelUtils(topics, getHeaderInfo(), getFormatInfo());
        excelUtils.sendHttpResponse(response, filename, excelUtils.getWorkbook());
    }

    // 获取表头信息
    private List<ExcelHeaderInfo> getHeaderInfo() {
        ExcelHeaderInfo one= new ExcelHeaderInfo(1, 1, 0, 0, "专题");
        ExcelHeaderInfo two= new ExcelHeaderInfo(1, 1, 1, 1, "姓名");
        ExcelHeaderInfo three=new ExcelHeaderInfo(1,1,2,2,"周报标题");
        ExcelHeaderInfo four=new ExcelHeaderInfo(1, 1, 3, 3, "周报内容");
        ExcelHeaderInfo five= new ExcelHeaderInfo(1, 1, 4,4, "问题");
        ExcelHeaderInfo six= new ExcelHeaderInfo(1, 1, 5,5, "下周计划");
        ExcelHeaderInfo[] my={one,two,three,four,five,six};
        return Arrays.asList(my);
    }

    // 获取格式化信息
    private Map<String, ExcelFormat> getFormatInfo() {
        Map<String, ExcelFormat> format = new HashMap<>();
        format.put("id", ExcelFormat.FORMAT_INTEGER);
        return format;
    }


}
