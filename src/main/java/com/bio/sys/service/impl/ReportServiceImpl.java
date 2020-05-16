package com.bio.sys.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public List<ReportDO> getThisWeekReportByDeptAndStatusLSub(Long deptId,Integer status) {
		List<ReportDO> reportListAll=baseMapper.getThisWeekReportByDept(deptId);
		//-1表示获取上周所有的专题内周报,0表示未提交（专题组长未审阅）的周报，1表示已审阅的周报
		if(status==-1) return reportListAll;
		else {
			List<ReportDO> reportList=new ArrayList<>();
			for (int i = 0; i < reportListAll.size(); i++) {
				ReportDO report=reportListAll.get(i);
				if (report.getStatusLSub() == status)
					reportList.add(report);
			}
			return reportList;
		}
	}
}
