package com.cym.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.model.User;
import com.cym.service.CmdService;
import com.cym.service.SettingService;
import com.cym.sqlhelper.utils.SqlHelper;
import com.cym.utils.LinuxTools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;

@Component
public class InitConfig {
	Logger logger = LoggerFactory.getLogger(InitConfig.class);
	@Inject
	HomeConfig homeConfig;
	@Inject
	VersionConfig versionConfig;
	@Inject("${project.findPass}")
	Boolean findPass;
	@Inject
	SqlHelper sqlHelper;
	@Inject
	CmdService cmdService;

	@Init
	public void init() {

		// 找回密码
		if (findPass) {
			List<User> users = sqlHelper.findAll(User.class);
			for (User user : users) {
				System.out.println("用户名:" + user.getName() + " 密码:" + user.getPass());
			}
			System.exit(1);
		}

		// 展示logo
		try {
			ClassPathResource resource = new ClassPathResource("banner.txt");
			BufferedReader reader = resource.getReader(Charset.forName("utf-8"));
			String str = null;
			StringBuilder stringBuilder = new StringBuilder();
			// 使用readLine() 比较方便的读取一行
			while (null != (str = reader.readLine())) {
				stringBuilder.append(str + "\n");
			}
			reader.close();// 关闭流

			stringBuilder.append("dockerWebUI " + versionConfig.currentVersion + "\n");

			logger.info(stringBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
