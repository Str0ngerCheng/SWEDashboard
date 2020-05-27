package com.bio.sys.service.impl;

import java.util.Date;
import java.util.List;

import org.omg.CORBA.BAD_CONTEXT;
import org.springframework.stereotype.Service;

import com.bio.common.base.CoreServiceImpl;
import com.bio.sys.dao.SummaryDao;
import com.bio.sys.domain.ReportDO;
import com.bio.sys.domain.SummaryDO;
import com.bio.sys.service.SummaryService;

@Service
public class SummaryServiceImpl extends CoreServiceImpl<SummaryDao, SummaryDO> implements SummaryService {

	@Override
	public List<ReportDO> selectListReportDOs(Long deptId, Date fromDate, Date toDate) {
		
		return baseMapper.selectListReportDOs(deptId, fromDate, toDate);
	
	}

	@Override
	public Integer getSummaryCountByTitle(String title) {
		
		return baseMapper.getSummaryCountByTitle(title);
	
	}

	@Override
	public List<SummaryDO> getThisWeekSummary() {
		return baseMapper.getThisWeekSummary();
	}

	@Override
	public List<SummaryDO> selectList( Date fromDate, Date toDate) {
		return baseMapper.selectList(fromDate,toDate);
	}

	@Override
	public List<SummaryDO> getReportsByMonth1(int date) {
		return baseMapper.getReportsByMonth1(date);
	}

}
