package com.bio.sys.service.impl;

import com.bio.common.base.CoreService;
import com.bio.common.base.CoreServiceImpl;
import com.bio.sys.dao.QueryDao;
import com.bio.sys.domain.QueryDO;
import com.bio.sys.service.QueryService;
import org.springframework.stereotype.Service;

@Service
public class QueryServiceImpl extends CoreServiceImpl<QueryDao, QueryDO> implements QueryService {

}
