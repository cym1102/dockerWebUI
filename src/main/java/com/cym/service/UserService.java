package com.cym.service;

import org.noear.solon.annotation.Inject;
import org.noear.solon.aspect.annotation.Service;

import com.cym.model.User;
import com.cym.sqlhelper.bean.Page;
import com.cym.sqlhelper.utils.ConditionAndWrapper;
import com.cym.sqlhelper.utils.ConditionOrWrapper;
import com.cym.sqlhelper.utils.SqlHelper;

import cn.hutool.core.util.StrUtil;

@Service
public class UserService {
	@Inject
	SqlHelper sqlHelper;

	public User login(String name, String pass) {
		ConditionAndWrapper conditionAndWrapper = new ConditionAndWrapper().eq(User::getName, name).eq(User::getPass, pass);

		return sqlHelper.findOneByQuery(conditionAndWrapper, User.class);
	}

	public Page<User> search(Page page, String keywords) {
		ConditionAndWrapper conditionAndWrapper = new ConditionAndWrapper();

		if (StrUtil.isNotEmpty(keywords)) {
			conditionAndWrapper.and(new ConditionOrWrapper().like(User::getName, keywords));
		}

		Page<User> pageResp = sqlHelper.findPage(conditionAndWrapper, page, User.class);

		return pageResp;
	}

	public void deleteById(String userId) {
		sqlHelper.deleteById(userId, User.class);
	}

	public User getByName(String name, String userId) {
		ConditionAndWrapper conditionAndWrapper = new ConditionAndWrapper().eq(User::getName, name);
		if (StrUtil.isNotEmpty(userId)) {
			conditionAndWrapper.ne(User::getId, userId);
		}

		return sqlHelper.findOneByQuery(conditionAndWrapper, User.class);
	}


}
