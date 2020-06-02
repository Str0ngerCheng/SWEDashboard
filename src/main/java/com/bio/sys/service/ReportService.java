package com.bio.sys.service;

import java.util.Date;
import java.util.List;

import com.bio.common.base.CoreService;
import com.bio.sys.domain.ReportCountDO;
import com.bio.sys.domain.ReportDO;
import com.bio.sys.vo.ReportScoreVO;

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

	List<ReportDO> getReportsQuery(Date fromDate, Date toDate, Integer status,long deptId);

	ReportDO getReportsByContentId(String contentId);

	List<ReportDO> getReportsByAutName(String AutName);

	List<ReportDO> getReportsAll();

	List<ReportDO> getReportsByMonth(int date,int date1,long deptId);

	ReportDO getReportsByTitle(String title);

	public ReportDO getThisWeekReportByAuthorId(Long authorId);

	public  List<ReportDO> getThisWeekReportByDept(Long deptId);

	public  List<ReportDO> getThisWeekReportByDeptAndStatusLSub(Long deptId,Integer status);

	public  List<ReportDO> getThisWeekReportByDeptAndStatusMSub(Long deptId,Integer status);

	public Boolean updateBatch(List<ReportDO> reportDOList);


	List<ReportDO> getReportsQuery1(Date fromDate, Date toDate, Integer status);

	List<ReportDO> getReportsByMonth1(int date,int date1);


	public List<ReportScoreVO> getMonthAVGReportScore();

	List<ReportScoreVO> getDelayPer();

}
