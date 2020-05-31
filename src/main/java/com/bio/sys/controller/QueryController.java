package com.bio.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.bio.common.service.ContextService;
import com.bio.common.utils.*;
import com.bio.common.utils.excel.ExcelFormat;
import com.bio.common.utils.excel.ExcelHeaderInfo;
import com.bio.sys.domain.*;
import com.bio.sys.service.DeptService;
import com.bio.sys.service.ReportContentService;
import com.bio.sys.service.ReportService;
import com.bio.sys.service.SummaryService;
import com.bio.sys.vo.MonthReportDetailsVO;
import com.bio.sys.vo.SummaryVO;
import com.bio.sys.vo.TopicReportDetailsVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/bio/query")
public class QueryController {
    @Autowired
    private ContextService contextService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportContentService reportContentService;

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private DeptService deptService;

    private static ConcurrentHashMap<Long,List<ReportDO>> excelreport=new ConcurrentHashMap<>();
    private static int weekly=0;
    private String directory = "E:\\Test\\";

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
    public Result<Page<QueryDO>> list(Integer pageNumber, Integer pageSize,ReportDO reportDTO) {
        UserDO userDO = contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        List<ReportDO> mylist=new ArrayList<>();
        if (userDO.getroleId().intValue() == 5 ) { // 超级管理员，默认所有的报告
            //TODO: 不做处理
           mylist = reportService.getReportsAll();
        }

        if (userDO.getroleId().intValue() == 2 || userDO.getroleId().intValue() == 3 || userDO.getroleId().intValue() == 4 ) { // 专题组长，只显示负责的学生 的报告
              String deptName=userDO.getDeptName();
              mylist=reportService.getReportsByDepName(deptName);
        }

        Page page1 = new Page(pageNumber, pageSize);
        page1.setTotal(mylist.size());
        int startIndex=(pageNumber-1)*pageSize;
        int endIndex=Math.min(startIndex+pageSize,mylist.size());
        page1.setRecords(mylist.subList(startIndex,endIndex));
        return Result.ok(page1);
    }

    @ResponseBody
    @GetMapping("/list1")
    public Result<Page<SummaryVO>> list(Integer pageNumber, Integer pageSize){
        List<SummaryDO> summaryDOList=summaryService.getThisWeekSummary();
        List<SummaryVO> summaryVOList=new ArrayList<>();
            for(SummaryDO summaryDO:summaryDOList){
                    DeptDO deptDO=deptService.selectById(summaryDO.getDeptId());
                    SummaryVO summaryVO=new SummaryVO(summaryDO,deptDO.getOrderNum());
                    summaryVOList.add(summaryVO);
                    List<ReportDO> mylist=new ArrayList<>();
                    mylist= reportService.getReportsQuery(summaryDO.getRFromDate(), summaryDO.getRToDate(), 1,deptDO.getId());
                    excelreport.put(deptDO.getId(), mylist);
                    weekly=0;
            }
        Page page1 = new Page(pageNumber, pageSize);
        page1.setTotal(summaryVOList.size());
        int startIndex=(pageNumber-1)*pageSize;
        int endIndex=Math.min(startIndex+pageSize,summaryVOList.size());
        page1.setRecords(summaryVOList.subList(startIndex,endIndex));
        return Result.ok(page1);
    }


    @ResponseBody
    @GetMapping("/queryReport")
    public Result<Page<QueryDO>> queryReport(Integer pageNumber, Integer pageSize, String userName, String datetimepicker1, String datetimepicker2, Integer WeekMonth) throws ParseException {
        datetimepicker1=datetimepicker1+" 00:00:00";
        datetimepicker2=datetimepicker2+" 23:59:59";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate=sdf.parse(datetimepicker1);
        Date endDate=sdf.parse(datetimepicker2);
        List<ReportDO> myreport=new ArrayList<>();
        List<ReportDO> thereport=new ArrayList<>();
        if (WeekMonth== 1) {
            myreport = reportService.getReportsQuery1(startDate, endDate, 1);
        }else{
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(startDate);
            int month=calendar.get(calendar.MONTH)+1;
            myreport = reportService.getReportsByMonth1(month);
        }
        for (ReportDO products : myreport) {
            if (products.getAuthorName().equals(userName)  || userName.equals("") || userName.equals("超级管理员")) {
                thereport.add(products);
            }
        }
        Page page1 = new Page(pageNumber, pageSize);
        page1.setTotal(thereport.size());
        int startIndex=(pageNumber-1)*pageSize;
        int endIndex=Math.min(startIndex+pageSize,thereport.size());
        page1.setRecords(thereport.subList(startIndex,endIndex));
        return Result.ok(page1);
    }


    @ResponseBody
    @GetMapping("/queryDepReport")
    public Result<Page<SummaryVO>> queryDepReport(Integer pageNumber, Integer pageSize, String topicName, String datetimepicker_t1, String datetimepicker_t2, Integer WeekMonth1) throws ParseException {
        datetimepicker_t1=datetimepicker_t1+" 00:00:00";
        datetimepicker_t2=datetimepicker_t2+" 23:59:59";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate=sdf.parse(datetimepicker_t1);
        Date endDate=sdf.parse(datetimepicker_t2);
        List<SummaryVO> summaryVOList=new ArrayList<>();
        List<ReportDO> mSubmitReportList=new ArrayList<>();
        List<SummaryDO> summaryDOList=new ArrayList<>();
        int month=0;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(startDate);
        if (WeekMonth1== 1) {
            summaryDOList=summaryService.selectList(startDate,endDate);
        }else{
            month=calendar.get(calendar.MONTH)+1;
            summaryDOList=summaryService.getReportsByMonth1(month);
        }
        long temp=12;
        for(SummaryDO summaryDO:summaryDOList)
       {
           if (summaryDO.getDeptName().equals(topicName) || topicName.equals("智慧地球")) {
               DeptDO deptDO = deptService.selectById(summaryDO.getDeptId());
               if(WeekMonth1==1){
                   SummaryVO summaryVO = new SummaryVO(summaryDO, deptDO.getOrderNum());
                   summaryVOList.add(summaryVO);
                   mSubmitReportList= reportService.getReportsQuery(summaryDO.getRFromDate(),summaryDO.getRToDate(), 1,deptDO.getId());
                   weekly=0;
               }else{
                   if(temp!=deptDO.getId()) {
                       String name=calendar.get(calendar.YEAR)+"-"+month+"-"+summaryDO.getDeptName()+" 月报";
                       SummaryVO summaryVO=new SummaryVO(summaryDO.getDeptId(),summaryDO.getDeptName(),name,temp,calendar.getTime(),deptDO.getOrderNum());
                       temp=deptDO.getId();
                       summaryVOList.add(summaryVO);
                       mSubmitReportList = reportService.getReportsByMonth(month, deptDO.getId());
                       weekly=month;
                   }
               }
               excelreport.put(deptDO.getId(), mSubmitReportList);
        }
    }
        Page page1 = new Page(pageNumber, pageSize);
        page1.setTotal(summaryVOList.size());
        int startIndex=(pageNumber-1)*pageSize;
        int endIndex=Math.min(startIndex+pageSize,summaryVOList.size());
        page1.setRecords(summaryVOList.subList(startIndex,endIndex));
        return Result.ok(page1);

    }


    //获取本周的专题周报信息
    @GetMapping("/weekInfo/{deptId}")
    public String getWeekInfo(@PathVariable("deptId")Long deptId,Model model) throws ParseException {
        List<ReportDO> mSubmitReportList=new ArrayList<>();
        mSubmitReportList=excelreport.get(deptId);
         DeptDO deptDO= deptService.selectById(deptId);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
        /*获取专题周报详细信息*/
        if(weekly==0) {
            List<TopicReportDetailsVO> topicReportDetailsList=new ArrayList<>();
            for(int i=0;i<mSubmitReportList.size();i++ ){
                TopicReportDetailsVO topicReportDetails=new TopicReportDetailsVO();
                ReportDO report=mSubmitReportList.get(i);
                topicReportDetails.setDeptName(report.getDeptName());
                topicReportDetails.setDeptOrder(deptDO.getOrderNum());
                topicReportDetails.setUserOrder(i);
                topicReportDetails.setAuthorName(report.getAuthorName());
                ReportContentDO reportContent=reportContentService.getByUUID(report.getContentId());
                topicReportDetails.setMonthPlan(reportContent.getMonthPlan());
                topicReportDetails.setSummary(reportContent.getSummary());
                topicReportDetails.setNextPlan(reportContent.getNextPlan());
                topicReportDetails.setProblem(reportContent.getProblem());
                topicReportDetails.setComment(report.getComment());
                topicReportDetailsList.add(topicReportDetails);
            }
            Collections.sort(topicReportDetailsList);//这里直接在后端排序
            model.addAttribute("topicReportDetailsList", topicReportDetailsList);
            Date mon = mSubmitReportList.get(0).getRFromDate();
            Date sun = mSubmitReportList.get(0).getRToDate();
            String title = sdf1.format(mon) + "-" + sdf1.format(sun) + "-" + deptDO.getName() + " 工作汇总";
            model.addAttribute("title", title);
            return "bio/query/weekInfo";
       }else {
            List<MonthReportDetailsVO> topicReportDetailsList=new ArrayList<>();
            for(int i=0;i<mSubmitReportList.size();i++ ){
                MonthReportDetailsVO topicReportDetails=new MonthReportDetailsVO();
                ReportDO report=mSubmitReportList.get(i);
                topicReportDetails.setDeptName(report.getDeptName());
                topicReportDetails.setDeptOrder(deptDO.getOrderNum());
                topicReportDetails.setUserOrder(i);
                topicReportDetails.setAuthorName(report.getAuthorName());
                topicReportDetails.setWeekDay(sdf1.format(report.getRFromDate())+"-"+sdf1.format(report.getRToDate()));
                ReportContentDO reportContent=reportContentService.getByUUID(report.getContentId());
                topicReportDetails.setMonthPlan(reportContent.getMonthPlan());
                topicReportDetails.setSummary(reportContent.getSummary());
                topicReportDetails.setNextPlan(reportContent.getNextPlan());
                topicReportDetails.setProblem(reportContent.getProblem());
                topicReportDetails.setComment(report.getComment());
                topicReportDetailsList.add(topicReportDetails);
            }
            Collections.sort(topicReportDetailsList);//这里直接在后端排序
            model.addAttribute("MonthReportDetailsList", topicReportDetailsList);
            String title=weekly+"月"+deptDO.getName()+"工作汇总";
            model.addAttribute("title", title);
            return "bio/query/MonthInfo";
       }
    }


    @ResponseBody
    @GetMapping("/searchKey")
    public Result<Page<ReportContentDO>> findByPageNo(Integer pageNumber, Integer pageSize, String searchKey){
        List<ReportContentDO> mylist=reportContentService.getSearchKey(searchKey);
        List<ReportDO> thereport=new ArrayList<>();
        for(ReportContentDO reportContentDO:mylist){
            ReportDO myreport= reportService.getReportsByContentId(reportContentDO.getUuid());
            thereport.add(myreport);
        }
        Page page1 = new Page(pageNumber, pageSize);
        page1.setTotal(thereport.size());
        int startIndex=(pageNumber-1)*pageSize;
        int endIndex=Math.min(startIndex+pageSize,thereport.size());
        page1.setRecords(thereport.subList(startIndex,endIndex));
        return Result.ok(page1);
    }

    @ResponseBody
    @GetMapping("/batchExport")
    @RequiresAuthentication
    //根据reportid批量导出，mode={1：汇总表+附件，2：汇总表，3：仅导出附件}
    public void BatchExport(@RequestParam(value="ids") String ids,@RequestParam(value="mode") Integer mode,@RequestParam(value="downloadfilename")String downloadfilename, HttpServletResponse response) {
        //String directory="E:\\Test\\";
        //String filename ="周报汇总表.xlsx";
        String filename =downloadfilename+"-汇总表.xlsx";
        String downloadzipname=downloadfilename+".zip";
        List<String> zipnames=new ArrayList<>();//需要压缩的文件名
        List<TopicDao> topics= new ArrayList<>();
        String[] myids=ids.split(",");
        List<String> mylist=Arrays.asList(myids);
        for(String list:mylist){
            ReportDO reportDO=reportService.selectById(Integer.parseInt(list));
            ReportContentDO reportContentDO=reportContentService.getByUUID(reportDO.getContentId());
            topics.add(new TopicDao(reportDO.getDeptName(),reportDO.getAuthorName(),reportDO.getTitle(),
                    reportContentDO.getSummary(),reportContentDO.getProblem(),reportContentDO.getNextPlan(),
                    reportDO.getComment(),reportDO.getSuggest()));
            //需要导出的附件名称
            String fujianname=reportDO.getTitle().replace('/','-')+ "附件.zip";
            zipnames.add(fujianname);
        }

        if(mode==1){
            ExcelUtils excelUtils = new ExcelUtils(topics, getHeaderInfo(), getFormatInfo());
            excelUtils.SaveExcelFile(directory+filename, excelUtils.getWorkbook());
            //打包下载文件
            zipnames.add(filename);
            String[] names=new String[zipnames.size()];
            zipnames.toArray(names);
            ZipUtils.downloadAllFilebyNames(response,directory,names,downloadzipname);
            //删除生成的汇总表
            ZipUtils.deleteFile(directory+filename);
            return;
        }
        if(mode==2){
            ExcelUtils excelUtils = new ExcelUtils(topics, getHeaderInfo(), getFormatInfo());
            excelUtils.sendHttpResponse(response,filename, excelUtils.getWorkbook());
            return;
        }
        if(mode==3){
            String[] names=new String[zipnames.size()];
            zipnames.toArray(names);
            downloadzipname=downloadfilename+"-附件.zip";
            ZipUtils.downloadAllFilebyNames(response,directory,names,downloadzipname);
            return;
        }
    }

    @ResponseBody
    @GetMapping("/batchExport1")
    @RequiresAuthentication
    public void BatchExport1(@RequestParam(value="ids") String ids,@RequestParam(value="mode") Integer mode,@RequestParam(value="downloadfilename")String downloadfilename, HttpServletResponse response) {
        //String directory="E:\\Test\\";
        //String filename ="周报汇总表.xlsx";
        String filename =downloadfilename+"-汇总表.xlsx";
        String downloadzipname=downloadfilename+".zip";
        List<String> zipnames=new ArrayList<>();//需要压缩的文件名
        List<TopicDao> topics= new ArrayList<>();
        String[] myids=ids.split(",");
        List<String> mylist=Arrays.asList(myids);
        for(String list:mylist){
            if(excelreport.containsKey(Long.parseLong(list))){
                List<ReportDO> mSubmitReportList=excelreport.get(Long.parseLong(list));
                for(ReportDO reportDO:mSubmitReportList){
                    ReportContentDO reportContentDO=reportContentService.getByUUID(reportDO.getContentId());
                    topics.add(new TopicDao(reportDO.getDeptName(),reportDO.getAuthorName(),reportDO.getTitle(),
                            reportContentDO.getSummary(),reportContentDO.getProblem(),reportContentDO.getNextPlan(),
                            reportDO.getComment(),reportDO.getSuggest()));
                    //需要导出的附件名称
                    String fujianname=reportDO.getTitle().replace('/','-')+ "附件.zip";
                    zipnames.add(fujianname);
                }
            }
        }

        if(mode==1){
            ExcelUtils excelUtils = new ExcelUtils(topics, getHeaderInfo(), getFormatInfo());
            excelUtils.SaveExcelFile(directory+filename, excelUtils.getWorkbook());
            //打包下载文件
            zipnames.add(filename);
            String[] names=new String[zipnames.size()];
            zipnames.toArray(names);
            ZipUtils.downloadAllFilebyNames(response,directory,names,downloadzipname);
            //删除生成的汇总表
            ZipUtils.deleteFile(directory+filename);
            return;
        }
        if(mode==2){
            ExcelUtils excelUtils = new ExcelUtils(topics, getHeaderInfo(), getFormatInfo());
            excelUtils.sendHttpResponse(response,filename, excelUtils.getWorkbook());
            return;
        }
        if(mode==3){
            String[] names=new String[zipnames.size()];
            zipnames.toArray(names);
            downloadzipname=downloadfilename+"-附件.zip";
            ZipUtils.downloadAllFilebyNames(response,directory,names,downloadzipname);
            return;
        }
    }

    @GetMapping(value = "/ifSingleFileExist" )
    @RequiresAuthentication
    @ResponseBody
    public Result<String> iFileExist(String filename){
        //String filenamedecode=java.net.URLDecoder.decode(filename);
//        for(String filename:filenames){
            File file = new File(directory+filename);
            // 如果文件路径所对应的文件存在,则返回ok
            if (file.exists()){
                return Result.ok();
            }
//        }
        return Result.fail("没有可下载的附件！");
    }

    @GetMapping(value = "/ifMutilFileExist" )
    @RequiresAuthentication
    @ResponseBody
    public Result<String> ifMutilFileExist(@RequestParam(value="reportids") String reportids){
        List<String> zipnames=new ArrayList<>();//需要压缩的文件名
        String[] myids=reportids.split(",");
        List<String> mylist=Arrays.asList(myids);
        for(String list:mylist){
            ReportDO reportDO=reportService.selectById(Integer.parseInt(list));
            //需要导出的附件名称
            String fujianname=reportDO.getTitle().replace('/','-')+ "附件.zip";
            zipnames.add(fujianname);
        }
        String[] filenames=new String[zipnames.size()];
        zipnames.toArray(filenames);
        for(String filename:filenames){
            File file = new File(directory+filename);
            // 如果文件路径所对应的文件存在,则返回ok
            if (file.exists()){
                return Result.ok();
            }
        }
        return Result.fail("没有可下载的附件！");
    }

    @GetMapping(value = "/ifTopicFileExist" )
    @RequiresAuthentication
    @ResponseBody
    public Result<String> ifFileExistTopic(@RequestParam(value="deptids") String deptids){
        //String filenamedecode=java.net.URLDecoder.decode(filename);
        List<String> zipnames=new ArrayList<>();//需要压缩的文件名
        String[] myids=deptids.split(",");
        List<String> mylist=Arrays.asList(myids);
        for(String list:mylist){
            if(excelreport.containsKey(Long.parseLong(list))){
                List<ReportDO> mSubmitReportList=excelreport.get(Long.parseLong(list));
                for(ReportDO reportDO:mSubmitReportList){
                    //需要导出的附件名称
                    String fujianname=reportDO.getTitle().replace('/','-')+ "附件.zip";
                    zipnames.add(fujianname);
                }
            }
        }
        String[] filenames=new String[zipnames.size()];
        zipnames.toArray(filenames);
        for(String filename:filenames){
            File file = new File(directory+filename);
            // 如果文件路径所对应的文件存在,则返回ok
            if (file.exists()){
                return Result.ok();
            }
        }
        return Result.fail("没有可下载的附件！");
    }

    // 获取表头信息
    private List<ExcelHeaderInfo> getHeaderInfo() {
        ExcelHeaderInfo one= new ExcelHeaderInfo(0, 0, 0, 0, "专题");
        ExcelHeaderInfo two= new ExcelHeaderInfo(0, 0, 1, 1, "姓名");
        ExcelHeaderInfo three=new ExcelHeaderInfo(0,0,2,2,"周报标题");
        ExcelHeaderInfo four=new ExcelHeaderInfo(0, 0, 3, 3, "周报内容");
        ExcelHeaderInfo five= new ExcelHeaderInfo(0,0, 4,4, "问题");
        ExcelHeaderInfo six= new ExcelHeaderInfo(0, 0, 5,5, "下周计划");
        ExcelHeaderInfo seven= new ExcelHeaderInfo(0, 0, 6,6, "组长评价");
        ExcelHeaderInfo eight= new ExcelHeaderInfo(0, 0, 7,7, "建议");
        ExcelHeaderInfo[] my={one,two,three,four,five,six,seven,eight};
        return Arrays.asList(my);
    }

    // 获取格式化信息
    private Map<String, ExcelFormat> getFormatInfo() {
        Map<String, ExcelFormat> format = new HashMap<>();
        format.put("id", ExcelFormat.FORMAT_INTEGER);
        return format;
    }


}
