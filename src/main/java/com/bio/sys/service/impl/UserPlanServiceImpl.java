package com.bio.sys.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bio.sys.dao.UserPlanDao;
import com.bio.sys.domain.UserPlanDO;
import com.bio.sys.service.UserPlanService;
import org.springframework.stereotype.Service;

@Service
public class UserPlanServiceImpl extends ServiceImpl<UserPlanDao, UserPlanDO> implements UserPlanService {
}
