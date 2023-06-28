package com.cym.utils;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;

public class DockerUtils {

//	public static String getIdByName(String name) {
//		try {
//			String rs = RuntimeUtil.execForStr("docker ps -aqf name=" + name).trim();
//			if (StrUtil.isNotEmpty(rs) && !rs.contains("Cannot connect to the Docker")) {
//				return rs;
//			}
//		} catch (Exception e) {
//			System.err.println("docker未安装或未运行");
//		}
//		return null;
//	}
	

	public static Boolean isRunByName(String name) {
		try {
			String rs = RuntimeUtil.execForStr("docker ps -qf name=" + name).trim();
			if (StrUtil.isNotEmpty(rs) && !rs.contains("Cannot connect to the Docker")) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("docker未安装或未运行");
		}
		return false;
	}

}
