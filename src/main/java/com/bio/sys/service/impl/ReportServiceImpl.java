package com.bio.sys.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bio.common.base.CoreServiceImpl;
import com.bio.sys.dao.ReportDao;
import com.bio.sys.domain.ReportCountDO;
import com.bio.sys.domain.ReportDO;
import com.bio.sys.service.DeptService;
import com.bio.sys.service.ReportService;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * <pre>
 * 周报
 * </pre>
 * <small> 2019-12-14 21:13:15 | chenx</small>
 */
@Service
public class ReportServiceImpl extends CoreServiceImpl<ReportDao, ReportDO> implements ReportService {

	@Autowired
	private DeptService deptService;


	@Override
	public ReportDO getLatestReport(Long authorid, Date rToDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return baseMapper.getLatestReport(authorid, sdf.format(rToDate));
		
	}

	@Override
	public Integer getReportCountByTitle(String title) {
		return baseMapper.getReportCountByTitle(title);
	}

	@Override
	public List<ReportDO> getReports(Date fromDate, Date toDate, Integer status) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return baseMapper.getReports(sdf.format(fromDate),  sdf.format(toDate),  status);
	}

	@Override
	public List<ReportCountDO> getReportsCount(Date fromDate, Date toDate, Integer status) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ReportCountDO> reportCountDOs =  baseMapper.getReportsCount(sdf.format(fromDate),  sdf.format(toDate),  status);
		
		if(null != reportCountDOs && !reportCountDOs.isEmpty()) {
			
			for (ReportCountDO reportCountDO : reportCountDOs) {
				reportCountDO.setDeptName(deptService.selectById( reportCountDO.getDeptId()).getName());
			}
		}
		return reportCountDOs;
	}

	@Override
	public  List<ReportDO> getReportsByDepName(String depName){
		return baseMapper.getReportsByDepName(depName);
	}

	@Override
	public List<ReportDO> getReportsQuery(Date fromDate, Date toDate, Integer status) {
		return baseMapper.getReportsQuery(fromDate,  toDate,  status);
	}

	@Override
	public ReportDO getReportsByContentId(String contentId) {
		return baseMapper.getReportsByContentId(contentId);
	}

	@Override
	public List<ReportDO> getReportsByAutName(String AutName) {
		return baseMapper.getReportsByAutName(AutName);
	}

	public List<ReportDO> getReportsAll(){
		return baseMapper.getReportsAll();
	}

	@Override
	public List<ReportDO> getReportsByMonth(int date,Integer status) {
		return baseMapper.getReportsByMonth(date,status);
	}

	@Override
	public ReportDO getReportsByTitle(String title) {
		return baseMapper.getReportsByTitle(title);
	}


}
