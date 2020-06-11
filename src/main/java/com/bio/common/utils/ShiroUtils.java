package com.bio.common.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.bio.sys.domain.UserDO;

public class ShiroUtils {
	public static Subject getSubjct() {
		SecurityUtils.getSubject().getSession().setTimeout(14400*1000);
		return SecurityUtils.getSubject();
	}
	public static UserDO getUser() {
		return (UserDO)getSubjct().getPrincipal();
	}
	public static Long getUserId() {
		return getUser().getId();
	}
	public static void logout() {
		getSubjct().logout();
	}
}
