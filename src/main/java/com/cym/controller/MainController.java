package com.cym.controller;

import java.io.File;
import java.io.IOException;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.config.HomeConfig;
import com.cym.model.User;
import com.cym.service.CmdService;
import com.cym.service.UserService;
import com.cym.utils.BaseController;
import com.cym.utils.JarUtil;
import com.cym.utils.JsonResult;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;

/**
 * 登录页
 * 
 * @author Useristrator
 *
 */
@Mapping("")
@Controller
public class MainController extends BaseController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	UserService userService;
	@Inject
	HomeConfig homeConfig;
	@Inject
	CmdService cmdService;

	@Mapping("/")
	public void jump(Context ctx) {
		ctx.redirect("/adminPage/login");
	}

	@Mapping("/adminPage/login")
	public ModelAndView admin() {
		ModelAndView modelAndView = new ModelAndView("/adminPage/login/index.html");
		modelAndView.put("adminCount", sqlHelper.findAllCount(User.class));
		return modelAndView;
	}

	@Mapping("/adminPage/login/login")
	public JsonResult submitLogin(String name, String pass, String code) {
		String captcha = (String) Context.current().session("captcha");
		if (!code.equals(captcha)) {
			Context.current().sessionRemove("captcha"); // 销毁验证码
			return renderError("验证码不正确"); // 验证码不正确
		}
		Context.current().sessionRemove("captcha"); // 销毁验证码

		User user = userService.login(name, pass);
		if (user == null) {
			return renderError("登录失败,请检查用户名密码");
		}

		if (user.getOpen() != 0) {
			return renderError("该用户已停用");
		}

		Context.current().sessionSet("user", user);

		// 检查更新
		versionConfig.checkVersion();

		return renderSuccess();
	}

	@Mapping("/adminPage/login/addAdmin")
	public JsonResult addAdmin(String trueName, String name, String pass) {

		Long adminCount = sqlHelper.findAllCount(User.class);
		if (adminCount > 0) {
			return renderError("管理员已存在");
		}
		User user = new User();
		user.setName(name);
		user.setPass(pass);
		sqlHelper.insert(user);

		return renderSuccess();
	}

	@Mapping("/adminPage/login/loginOut")
	public void loginOut(Context ctx) {
		ctx.redirect("/adminPage/login");
	}

	@Mapping("/adminPage/login/getCode")
	public void getCode() throws Exception {
		Context.current().headerAdd("Pragma", "No-cache");
		Context.current().headerAdd("Cache-Control", "no-cache");
		Context.current().headerAdd("Expires", "0");
		Context.current().contentType("image/gif");

		SpecCaptcha specCaptcha = new SpecCaptcha(100, 40, 4);
		specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

		Context.current().sessionSet("captcha", specCaptcha.text().toLowerCase());
		specCaptcha.out(Context.current().outputStream());
	}

	@Mapping("/adminPage/main/upload")
	public JsonResult upload(Context context, UploadedFile file) {
		try {
			File temp = new File(FileUtil.getTmpDir() + "/" + file.getName().replace(" ", "_"));
			file.transferTo(temp);

			// 移动文件
			File dest = new File(homeConfig.temp + file.getName().replace(" ", "_"));
			FileUtil.move(temp, dest, true);

			String path = dest.getPath();

			return renderSuccess(path);
		} catch (IllegalStateException | IOException e) {
			logger.error(e.getMessage(), e);
		}

		return renderError();
	}

//	@Mapping("/adminPage/main/autoUpdate")
//	public JsonResult autoUpdate(String url) {
//
//		File jar = JarUtil.getCurrentFile();
//		String path = jar.getParent() + "/sora.jar.update";
//		logger.info("url:" + url);
//		logger.info("download:" + path);
//		HttpUtil.downloadFile(url, path);
//		updateUtils.run(path);
//		return renderSuccess();
//	}
}
