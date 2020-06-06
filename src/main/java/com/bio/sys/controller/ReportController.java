package com.bio.sys.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bio.common.service.ContextService;
import com.bio.common.utils.Result;
import com.bio.sys.domain.ReportContentDO;
import com.bio.sys.domain.ReportDO;
import com.bio.sys.domain.UserDO;
import com.bio.sys.service.PlaceholderService;
import com.bio.sys.service.ReportContentService;
import com.bio.sys.service.ReportService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * <pre>
 * 周报
 * </pre>
 * <small> 2019-12-14 21:13:15 | chenx</small>
 */
@Controller
@RequestMapping("/bio/report")
public class ReportController {
	@Autowired
	private ReportService reportService;

	@Autowired
	private ReportContentService reportContentService;

	@Autowired
	private ContextService contextService;

	@Autowired
	private PlaceholderService placeholderService;

	@GetMapping()
	@RequiresPermissions("bio:report:report")
	String Report(){
		return "bio/report/report";
	}

	@ResponseBody
	@GetMapping("/latest/{id}")
	@RequiresPermissions("bio:report:report")
	public Result<ReportDO>  getLatestReport(@PathVariable("id") Integer id){

		UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());

		ReportDO report = reportService.selectById(id);
		ReportDO reportDO = null;
		if(null != report) {
			reportDO = reportService.getLatestReport(userDO.getId(), report.getRFromDate());
		}
		if(null != reportDO){
			return Result.ok(reportDO);
		}
		return Result.fail();
	}

	/*
	 * 显示自己的周报列表，按时间排序。超级管理员和陈老师不显示
	 * */
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("bio:report:report")
	public Result<Page<ReportDO>> list(Integer pageNumber, Integer pageSize, ReportDO reportDTO){

		UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());

		if(userDO.getroleId().intValue() == 1) { // 超级管理员，默认所有的报告
			//TODO: 不做处理
		}
		else
			reportDTO.setAuthorId(userDO.getId());

		// 查询列表数据
		Page<ReportDO> page = new Page<>(pageNumber, pageSize);

		Wrapper<ReportDO> wrapper = new EntityWrapper<ReportDO>(reportDTO);
		wrapper.orderBy("r_create_date", false);
		page = reportService.selectPage(page, wrapper);
		int total = reportService.selectCount(wrapper);
		page.setTotal(total);
		return Result.ok(page);
	}

	@GetMapping("/add")
	@RequiresPermissions("bio:report:add")
	String add(){
		return "bio/report/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("bio:report:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		ReportDO report = reportService.selectById(id);
		ReportContentDO reportContent = reportContentService.getByUUID(report.getContentId());

		UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());

		model.addAttribute("reportContent", reportContent);
		model.addAttribute("report", report);
		return "bio/report/edit";
	}

	@GetMapping("/getLastReport/{id}")
	@RequiresPermissions("bio:report:edit")
	String getLastReport(@PathVariable("id") Integer id,Model model){
		UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
		ReportDO lastReport = reportService.getLastReport(userDO.getId(),new Date());
		ReportDO report = reportService.selectById(id);
		ReportContentDO reportContent;
		if(lastReport!=null){
			reportContent = reportContentService.getByUUID(lastReport.getContentId());
			model.addAttribute("reportContent", reportContent);
			model.addAttribute("report", report);
		}
		else {
			reportContent=reportContentService.getByUUID(report.getContentId());
			model.addAttribute("reportContent", reportContent);
			model.addAttribute("report", report);
		}

		return "bio/report/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("bio:report:add")
	public Result<String> save( ReportDO report){
		if(reportService.insert(report)){
			return Result.ok();
		}
		return Result.fail();
	}
	/**
	 * 保存周报内容
	 */
	@ResponseBody
	@PostMapping("/saveContent")
	@RequiresPermissions("bio:report:edit")
	public Result<String> saveContent( Integer reportId,Integer statusMSub,
									   String lastPlan,String summary,String nextPlan,String problem){
		ReportDO report=reportService.selectById(reportId);
		if(report!=null) {
			if(statusMSub==1)
				report.setStatusMSub(statusMSub);//表示提交周报，提交后不能修改
			ReportContentDO reportContent = new ReportContentDO(report.getContentId(), lastPlan, summary, nextPlan, problem);
			if (reportContentService.updateByUUID(reportContent)) {
				report.setRChgDate(new Date());
				report.setStatusMod(1);
				reportService.updateById(report);
			}
			return Result.ok();
		}
		return Result.fail();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("bio:report:edit")
	public Result<String>  update(@RequestBody ReportDO report){
		report.setStatusLSub(1);
		reportService.updateById(report);
		return Result.ok();
	}


	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("bio:report:remove")
	public Result<String>  remove(Long id){
		if(reportService.deleteById(id)){
			return Result.ok();
		}
		return Result.fail();
	}

	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("bio:report:batchRemove")
	public Result<String>  remove(@RequestParam("ids[]") Integer[] ids){
		reportService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
	/**
	 * 仅显示周报内容
	 */
	@GetMapping("/reportContent/{id}")
	String getReportContent(@PathVariable("id") Integer id,Model model){
		ReportDO report = reportService.selectById(id);
		ReportContentDO reportContent = reportContentService.getByUUID(report.getContentId());
		model.addAttribute("reportContent", reportContent);
		model.addAttribute("report", report);
		model.addAttribute("mark", false);
		return "bio/report/reportContent";
	}

	/**
	 * 显示周报内容,同时显示打分和评价模块（专题组长专用）
	 */
	@GetMapping("/markReportContent/{id}")
	@RequiresPermissions("bio:report:report")
	String getMarkReportContent(@PathVariable("id") Integer id,Model model){
		ReportDO report = reportService.selectById(id);
		ReportContentDO reportContent = reportContentService.getByUUID(report.getContentId());
		model.addAttribute("reportContent", reportContent);
		model.addAttribute("report", report);
		model.addAttribute("mark", true);
		return "bio/report/reportContent";
	}

	@ResponseBody
	@GetMapping("/searchKey")
	@RequiresPermissions("bio:report:report")
	public Result<Page<ReportContentDO>> findByPageNo(Integer pageNumber, Integer pageSize, String searchKey){
		List<ReportContentDO> mylist=reportContentService.getSearchKey(searchKey);
		UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
		List<ReportDO> thereport=new ArrayList<>();
		for(ReportContentDO reportContentDO:mylist){
			ReportDO myreport= reportService.getReportsByContentId(reportContentDO.getUuid());
			if(userDO.getName().equals(myreport.getAuthorName()))
			   thereport.add(myreport);
		}
		Page page1 = new Page(pageNumber, pageSize);
		page1.setTotal(thereport.size());
		int startIndex=(pageNumber-1)*pageSize;
		int endIndex=Math.min(startIndex+pageSize,thereport.size());
		page1.setRecords(thereport.subList(startIndex,endIndex));
		return Result.ok(page1);
	}

}
