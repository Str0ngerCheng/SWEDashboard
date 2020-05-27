package com.bio.sys.dao;

import java.util.List;

import com.bio.common.base.BaseDao;
import com.bio.sys.domain.DeptDO;
import org.apache.ibatis.annotations.Param;

/**
 * 部门查询 Dao
 * 
 * 
 * @author chenx
 *
 */
public interface DeptDao extends BaseDao<DeptDO> {
	
	Long[] listParentDept();
	
	int getDeptUserNumber(Long deptId);
	
	DeptDO getParentDept(Long deptId);
	
	List<DeptDO> getSubDepts(Long deptId);

	DeptDO getDeptByName(@Param("Name") String Name);
}
