package com.bio.sys.service;

import com.bio.common.base.CoreService;
import com.bio.sys.domain.ReportContentDO;


public interface ReportContentService extends CoreService<ReportContentDO> {

    public Boolean updateByUUID(ReportContentDO reportContent);
    public ReportContentDO getByUUID(String uuid);
	
}
