package com.bio.sys.dao;

import com.bio.common.base.BaseDao;
import com.bio.sys.domain.UserPlanDO;
import org.apache.ibatis.annotations.Param;


public interface UserPlanDao extends BaseDao<UserPlanDO> {

    UserPlanDO getByAuthorId(@Param("authorId") Long authorId);

}
