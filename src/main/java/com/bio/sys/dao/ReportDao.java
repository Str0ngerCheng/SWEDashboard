package com.bio.sys.dao;

import java.util.Date;
import java.util.List;

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

	public  ReportDO getLatestReport(@Param("authorid") Long authorid, @Param("rfromdate") String rToDate);

	public Integer getReportCountByTitle(String title);
	
	public List<ReportDO>getReports(@Param("fromdate") String fromDate,@Param("todate") String toDate, @Param("status") Integer status);
	
	 List<ReportCountDO> getReportsCount(@Param("fromdate") String fromDate,@Param("todate") String toDate, @Param("status") Integer status);

	 List<ReportDO> getReportsByDepName(@Param("depName") String depName);

	List<ReportDO> getReportsQuery(@Param("fromdate") Date fromDate, @Param("todate") Date toDate, @Param("status") Integer status);

	ReportDO getReportsByContentId(@Param("contentId")String contentId);

	List<ReportDO> getReportsByAutName(@Param("AutName") String AutName);

	List<ReportDO> getReportsAll();

	List<ReportDO>getReportsByMonth(@Param("date") int date, @Param("status") Integer status);

	ReportDO getReportsByTitle(@Param("title")String title);

}
