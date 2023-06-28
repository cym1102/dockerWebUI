package com.cym.model;

import com.cym.sqlhelper.bean.BaseModel;
import com.cym.sqlhelper.config.InitValue;
import com.cym.sqlhelper.config.Table;

/**
 * 用户
 * 
 * @author 陈钇蒙
 *
 */
@Table
public class User extends BaseModel {

	// 登录名
	String name;
	// 密码
	String pass;

	// 状态 0:启用 1:停用
	@InitValue("0")
	Integer open;

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}
