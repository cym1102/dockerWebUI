package com.cym.config;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.model.User;
import com.cym.sqlhelper.bean.Page;
import com.cym.utils.LinuxTools;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;

@Component
public class AppFilter implements Filter {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Inject
	VersionConfig versionConfig;

	@Override
	public void doFilter(Context ctx, FilterChain chain) throws Throwable {
		if (ctx.path().contains("adminPage") //
				&& !ctx.path().contains("/adminPage/login") //
				&& !ctx.path().endsWith(".js") //
				&& !ctx.path().endsWith(".css") //
				&& !ctx.path().endsWith(".jpg") //
				&& !ctx.path().endsWith(".png") //
		) {
			// 检查登录
			User user = (User) ctx.session("user");
			if (user == null) {
				ctx.redirect("/adminPage/login");
				return;
			}

		}

		ctx.attrSet("jsrandom", System.currentTimeMillis());
		ctx.attrSet("currentVersion", versionConfig.currentVersion);
		ctx.attrSet("ctx", getCtxStr(ctx));
		ctx.attrSet("page", new Page<>());
		ctx.attrSet("user", ctx.session("user"));

//		// 显示版本更新
//		if (versionConfig.newVersion != null) {
//			ctx.attrSet("newVersion", versionConfig.newVersion);
//
//			if (Integer.parseInt(versionConfig.currentVersion.replace(".", "").replace("v", "")) < Integer.parseInt(versionConfig.newVersion.getVersion().replace(".", "").replace("v", ""))) {
//				ctx.attrSet("hasNewVersion", 1);
//			}
//		}

		// 检查是否安装docker
		if (LinuxTools.isLinux()) {
			String rs = RuntimeUtil.execForStr("which docker").trim();
			if (StrUtil.isEmpty(rs) || rs.contains("no") && rs.contains("in")) {
				ctx.attrSet("hasDocker", 0);
			} else {
				ctx.attrSet("hasDocker", 1);
			}
		}

		chain.doFilter(ctx);

	}

	public String getCtxStr(Context context) {
		String httpHost = context.header("X-Forwarded-Host");
		String realPort = context.header("X-Forwarded-Port");
		String host = context.header("Host");

		String ctx = "//";
		if (StrUtil.isNotEmpty(httpHost)) {
			ctx += httpHost;
		} else if (StrUtil.isNotEmpty(host)) {
			ctx += host;
			if (!host.contains(":") && StrUtil.isNotEmpty(realPort)) {
				ctx += ":" + realPort;
			}
		} else {
			host = context.url().split("/")[2];
			ctx += host;
			if (!host.contains(":") && StrUtil.isNotEmpty(realPort)) {
				ctx += ":" + realPort;
			}
		}
		return ctx;

	}
}