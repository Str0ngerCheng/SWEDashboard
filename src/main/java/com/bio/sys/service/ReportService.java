package com.bio.sys.service;

import java.util.Date;
import java.util.List;

import com.bio.common.base.CoreService;
import com.bio.sys.domain.ReportCountDO;
import com.bio.sys.domain.ReportDO;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * <pre>
 * 周报
 * </pre>
 * <small> 2019-12-14 21:13:15 | chenx</small>
 */
public interface ReportService extends CoreService<ReportDO> {
    
	public  ReportDO getLatestReport(Long id, Date date);
	
	public  Integer getReportCountByTitle(String title);
	
	public List<ReportDO> getReports(Date fromDate, Date toDate, Integer status);
	
	public List<ReportCountDO> getReportsCount(Date fromDate, Date toDate, Integer status);

	List<ReportDO> getReportsByDepName(String depName);

	List<ReportDO> getReportsQuery(Date fromDate, Date toDate, Integer status);

	ReportDO getReportsByContentId(String contentId);

	List<ReportDO> getReportsByAutName(String AutName);

	List<ReportDO> getReportsAll();

	List<ReportDO> getReportsByMonth(int date,Integer status);

	ReportDO getReportsByTitle(String title);

}
