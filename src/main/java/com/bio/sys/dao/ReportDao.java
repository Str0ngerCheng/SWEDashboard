package com.bio.sys.dao;

import java.util.Date;
import java.util.List;

import com.bio.sys.vo.ReportScoreVO;
import org.apache.ibatis.annotations.Param;

import com.bio.common.base.BaseDao;
import com.bio.sys.domain.ReportCountDO;
import com.bio.sys.domain.ReportDO;
import org.joda.time.DateTime;

/**
 * 
 * <pre>
 * 周报
 * </pre>
 * <small> 2019-12-14 21:13:15 | chenx</small>
 */
public interface ReportDao extends BaseDao<ReportDO> {

	public  ReportDO getLatestReport(@Param("authorid") Long authorid, @Param("rfromdate") String rfromdate);

	public Integer getReportCountByTitle(String title);
	
	public List<ReportDO>getReports(@Param("fromdate") String fromDate,@Param("todate") String toDate, @Param("status") Integer status);

	 List<ReportCountDO> getReportsCount(@Param("fromdate") String fromDate,@Param("todate") String toDate, @Param("status") Integer status);

	 List<ReportDO> getReportsByDepName(@Param("depName") String depName);

	List<ReportDO> getReportsQuery(@Param("fromdate") Date fromDate, @Param("todate") Date toDate, @Param("status") Integer status, @Param("deptId")long deptId);

	ReportDO getReportsByContentId(@Param("contentId")String contentId);

	List<ReportDO> getReportsByAutName(@Param("AutName") String AutName);

	List<ReportDO> getReportsAll();

	List<ReportDO>getReportsByMonth(@Param("date") int date,@Param("deptId")long deptId);

	ReportDO getReportsByTitle(@Param("title")String title);

	public  List<ReportDO> getThisWeekReportByDept(@Param("deptId") Long deptId);

	public ReportDO getThisWeekReportByAuthorId(@Param("authorId") Long authorId);

	public Boolean updateBatch(@Param("reportDOList") List<ReportDO> reportDOList);


	List<ReportDO> getReportsQuery1(@Param("fromdate") Date fromDate, @Param("todate") Date toDate, @Param("status") Integer status);

	List<ReportDO>getReportsByMonth1(@Param("date") int date);

	public List<ReportScoreVO> getMonthAVGReportScore();

}
