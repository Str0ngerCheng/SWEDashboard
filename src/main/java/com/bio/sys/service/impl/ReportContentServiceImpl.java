package com.bio.sys.service.impl;

import com.bio.common.base.CoreServiceImpl;
import com.bio.sys.dao.ReportContentDao;
import com.bio.sys.domain.ReportContentDO;
import com.bio.sys.service.ReportContentService;
import org.springframework.stereotype.Service;


@Service
public class ReportContentServiceImpl extends CoreServiceImpl<ReportContentDao, ReportContentDO> implements ReportContentService {

    @Override
    public Boolean updateByUUID(ReportContentDO reportContent) {
        return baseMapper.updateByUUID(reportContent);
    }

    @Override
    public ReportContentDO getByUUID(String uuid) {
        return baseMapper.getByUUID(uuid);
    }
}
