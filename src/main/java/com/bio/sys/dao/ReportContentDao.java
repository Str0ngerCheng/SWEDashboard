package com.bio.sys.dao;

import com.bio.common.base.BaseDao;
import com.bio.sys.domain.ReportContentDO;
import org.apache.ibatis.annotations.Param;


public interface ReportContentDao extends BaseDao<ReportContentDO> {

    public Boolean updateByUUID(@Param("reportContent")ReportContentDO reportContent);
    public ReportContentDO getByUUID(@Param("uuid")String uuid);

}
