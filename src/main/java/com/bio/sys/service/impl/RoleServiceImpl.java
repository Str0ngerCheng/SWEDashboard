package com.bio.sys.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bio.sys.dao.RoleDao;
import com.bio.sys.dao.RoleMenuDao;
import com.bio.sys.dao.UserDao;
import com.bio.sys.dao.UserRoleDao;
import com.bio.sys.domain.RoleDO;
import com.bio.sys.domain.RoleMenuDO;
import com.bio.sys.service.RoleService;

/**
 * <pre>
 * </pre>
 * <small> 2018年3月23日 | Aron</small>
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleDO> implements RoleService {

    public static final String ROLE_ALL_KEY = "\"role_all\"";

    public static final String DEMO_CACHE_NAME = "role";

    @Autowired
    RoleMenuDao roleMenuMapper;
    @Autowired
    UserDao userMapper;
    @Autowired
    UserRoleDao userRoleMapper;

    @Cacheable(value = DEMO_CACHE_NAME, key = ROLE_ALL_KEY)
    @Override
    public List<RoleDO> findAll() {
        List<RoleDO> roles = selectList(null);
        return roles;
    }

	@Override
	public Long findRoleIdByUserId(Serializable id) {
	    Long rolesIds = userRoleMapper.findRoleId(id);
	    return rolesIds;
	}

	//查找权限低于该用户的角色列表
    @Override
    public List<RoleDO> findListByUserId(Serializable userId) {
        RoleDO role=userRoleMapper.findRoleByUserId(userId);
        List<RoleDO> roles = selectList(null);
        List<RoleDO> lowerRoles=new ArrayList<>();
        for (RoleDO roleDO : roles) {
            if(roleDO.getRoleLevel()>role.getRoleLevel())
                lowerRoles.add(roleDO);
        }
        return lowerRoles;
    }

    @CacheEvict(value = DEMO_CACHE_NAME, key = ROLE_ALL_KEY)
    @Transactional
    @Override
    public boolean insert(RoleDO role) {
        int count = baseMapper.insert(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getId();
        List<RoleMenuDO> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuDO rmDo = new RoleMenuDO();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        roleMenuMapper.removeByRoleId(roleId);
        if (rms.size() > 0) {
            roleMenuMapper.batchSave(rms);
        }
        return retBool(count);
    }

    @CacheEvict(value = DEMO_CACHE_NAME, key = ROLE_ALL_KEY)
    @Transactional
    @Override
    public boolean deleteById(Serializable id) {
        int count = baseMapper.deleteById(id);
        roleMenuMapper.removeByRoleId(id);
        return retBool(count);
    }

    @CacheEvict(value = DEMO_CACHE_NAME, key = ROLE_ALL_KEY)
    @Override
    public boolean updateById(RoleDO role) {
        int r = baseMapper.updateById(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getId();
        roleMenuMapper.removeByRoleId(roleId);
        List<RoleMenuDO> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuDO rmDo = new RoleMenuDO();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        if (rms.size() > 0) {
            roleMenuMapper.batchSave(rms);
        }
        return retBool(r);
    }

}
