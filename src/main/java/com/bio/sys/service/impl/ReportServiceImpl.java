package com.bio.sys.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.bio.sys.vo.ReportScoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bio.common.base.CoreServiceImpl;
import com.bio.sys.dao.ReportDao;
import com.bio.sys.domain.ReportCountDO;
import com.bio.sys.domain.ReportDO;
import com.bio.sys.service.DeptService;
import com.bio.sys.service.ReportService;

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
	public ReportDO getLastReport(Long authorid, Date rToDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return baseMapper.getLastReport(authorid, sdf.format(rToDate));

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
	public List<ReportDO> getReportsByUseName(String useName) {
		return baseMapper.getReportsByUseName(useName);
	}

	@Override
	public List<ReportDO> getReportsQuery(Date fromDate, Date toDate, Integer status,long deptId) {
		return baseMapper.getReportsQuery(fromDate,  toDate,  status,deptId);
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
	public List<ReportDO> getReportsByMonth(int date,int date1,long deptId) {
		return baseMapper.getReportsByMonth(date,date1,deptId);
	}

	@Override
	public ReportDO getReportsByTitle(String title) {
		return baseMapper.getReportsByTitle(title);
	}



	public ReportDO getThisWeekReportByAuthorId(Long authorId) {
		return baseMapper.getThisWeekReportByAuthorId(authorId);
	}

	public List<ReportDO> getThisWeekReportByDept(Long deptId) {
		return baseMapper.getThisWeekReportByDept(deptId);
	}


	@Override
	public List<ReportDO> getThisWeekReportByDeptAndStatusLSub(Long deptId,Integer status) {
		List<ReportDO> reportListAll=baseMapper.getThisWeekReportByDept(deptId);
		//-1表示获取上周所有提交的的专题内的周报,0表示提交了但专题组长未审阅的周报，1表示已审阅的周报
		List<ReportDO> reportList=new ArrayList<>();
		for (int i = 0; i < reportListAll.size(); i++) {
			ReportDO report=reportListAll.get(i);
			if(report.getStatusMSub()==1){//表示学生已提交周报
				if(status==-1)//获取所有
					reportList.add(report);
				else  if (report.getStatusLSub() == status)
					reportList.add(report);
			}
		}
		return reportList;
	}

	@Override
	public List<ReportDO> getThisWeekReportByDeptAndStatusMSub(Long deptId, Integer status) {
		//获取专题内所有的本周周报
		List<ReportDO> reportListAll=baseMapper.getThisWeekReportByDept(deptId);
		//status：0表示未提交，1表示已提交
		List<ReportDO> reportList=new ArrayList<>();
		for (int i = 0; i < reportListAll.size(); i++) {
			ReportDO report=reportListAll.get(i);
			if(report.getStatusMSub()==status){
				reportList.add(report);
			}
		}
		return reportList;
	}

	@Override
	public Boolean updateBatch(List<ReportDO> reportDOList) {
		return baseMapper.updateBatch(reportDOList);
	}

	@Override
	public List<ReportDO> getReportsQuery1(Date fromDate, Date toDate, Integer status) {
		return baseMapper.getReportsQuery1(fromDate,toDate,status);
	}

	@Override
	public List<ReportDO> getReportsByMonth1(int date,int date1) {
		return baseMapper.getReportsByMonth1(date,date1);
	}


	public List<ReportScoreVO> getMonthAVGReportScore() {
		return baseMapper.getMonthAVGReportScore();
	}

	@Override
	public List<ReportScoreVO> getDelayPer() {
		return baseMapper.getDelayPer();
	}

}
