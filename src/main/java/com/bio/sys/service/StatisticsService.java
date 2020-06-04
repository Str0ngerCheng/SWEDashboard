package com.bio.sys.service;

import com.bio.common.base.CoreService;
import com.bio.sys.domain.StatisticalDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatisticsService extends CoreService<StatisticalDO> {
   int insertStatistics(String userName, Double absenceRate, Date rFromDate,Date rToDate);
   List<StatisticalDO> queryByDate(int date,int date1);
   int deleteByDate(@Param("date")int date,@Param("date1")int date1);
}
