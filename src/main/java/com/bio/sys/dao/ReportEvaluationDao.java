package com.bio.sys.dao;

import org.apache.ibatis.annotations.Param;

public interface ReportEvaluationDao {

    public ReportEvaluationDao getEvaluationByReport(@Param("reportId")Integer reportId);
}
