package com.bio.sys.service;



import com.bio.common.base.CoreService;
import com.bio.sys.domain.ReportContentDO;

import java.util.List;



public interface ReportContentService extends CoreService<ReportContentDO> {

    public Boolean updateByUUID(ReportContentDO reportContent);
    public ReportContentDO getByUUID(String uuid);

    List<ReportContentDO> getSearchKey(String searchKey);

}
