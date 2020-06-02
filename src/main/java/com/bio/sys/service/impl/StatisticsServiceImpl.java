package com.bio.sys.service.impl;

import com.bio.common.base.CoreServiceImpl;
import com.bio.sys.dao.StatisticsDao;
import com.bio.sys.domain.StatisticalDO;
import com.bio.sys.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StatisticsServiceImpl extends CoreServiceImpl<StatisticsDao, StatisticalDO> implements StatisticsService {

    @Override
    public int insertStatistics(String userName, Double absenceRate, Date rFromDate, Date rToDate) {
        return baseMapper.insertStatistics(userName,absenceRate,rFromDate,rToDate);
    }

    @Override
    public List<StatisticalDO> queryByDate(int date, int date1) {
        return baseMapper.queryByDate(date,date1);
    }

    @Override
    public int deleteByDate(int date, int date1) {
        return baseMapper.deleteByDate(date,date1);
    }


}
