package com.bio.sys.dao;

import com.bio.common.base.BaseDao;
import com.bio.sys.domain.StatisticalDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatisticsDao extends BaseDao<StatisticalDO> {
   int insertStatistics (@Param("userName")String userName, @Param("absenceRate")Double absenceRate, @Param("rFromDate")Date rFromDate,@Param("rToDate")Date rToDate);
   List<StatisticalDO> queryByDate(@Param("date")int date,@Param("date1")int date1);
   int deleteByDate(@Param("date")int date,@Param("date1")int date1);
}
