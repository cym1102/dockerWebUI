package com.cym.config;

import java.io.File;

import javax.annotation.PostConstruct;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.utils.FilePermissionUtil;
import com.cym.utils.JarUtil;
import com.cym.utils.ToolUtils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

@Component
public class HomeConfig {
	@Inject("${project.home}")
	public String home;
	// 其他目录
	public String html;
	public String tomcat;
	public String nginx;
	public String java;
	public String jdk;
	public String go;
	public String acme;
	public String temp;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Init
	public void init() {
		if (StrUtil.isEmpty(home)) {
			// 获取jar位置
			File file = new File(JarUtil.getCurrentFilePath());

			if (file.getPath().contains("target") && file.getPath().contains("classes")) {
				home = FileUtil.getUserHomePath() + File.separator + "dockerWebUI";
			} else {
				home = file.getParent();
			}
		}

		// 修改反斜杠, 检查如果最后没有/, 加上/
		home = ToolUtils.endDir(ToolUtils.handlePath(home));

		// 检查路home权限
		if (!FilePermissionUtil.canWrite(new File(home))) {
			logger.error(home + " 目录没有可写权限,请重新指定.");
			System.exit(1);
		}

//		// 各个文件夹路径
//		acme = home + "acme/";
//
//		nginx = home + "nginx/";
//
//		html = home + "html/";
//
//		tomcat = home + "tomcat/";
//
//		java = home + "java/";
//
//		jdk = home + "jdk/";
//
//		go = home + "go/";
//
//		temp = home + "temp/";
//
//		// 创建需要的文件夹
//		if (!FileUtil.exist(html)) {
//			FileUtil.mkdir(html);
//		}
//		if (!FileUtil.exist(tomcat)) {
//			FileUtil.mkdir(tomcat);
//		}
//		if (!FileUtil.exist(java)) {
//			FileUtil.mkdir(java);
//		}
//		if (!FileUtil.exist(jdk)) {
//			FileUtil.mkdir(jdk);
//		}
//		if (!FileUtil.exist(go)) {
//			FileUtil.mkdir(go);
//		}
//		if (!FileUtil.exist(temp)) {
//			FileUtil.mkdir(temp);
//		}
//		if (!FileUtil.exist(nginx)) {
//			FileUtil.mkdir(nginx);
//		}
//		if (!FileUtil.exist(acme)) {
//			FileUtil.mkdir(acme);
//		}
	}

}
