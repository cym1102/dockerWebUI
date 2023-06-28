package com.cym.utils;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;

public class LinuxTools {

	public static boolean isApt() {
		try {
			String rs = RuntimeUtil.execForStr("which apt");
			return StrUtil.isNotEmpty(rs) && !rs.contains("no apt in");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isYum() {
		try {
			String rs = RuntimeUtil.execForStr("which yum");
			return StrUtil.isNotEmpty(rs) && !rs.contains("no yum in");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isLinux() {
		if (SystemUtil.get(SystemUtil.OS_NAME).toLowerCase().contains("windows")) {
			return false;
		} else if (SystemUtil.get(SystemUtil.OS_NAME).toLowerCase().contains("mac os")) {
			return false;
		} else {
			return true;
		}
	}
}
