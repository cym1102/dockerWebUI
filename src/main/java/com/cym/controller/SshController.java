package com.cym.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

import com.cym.utils.BaseController;

@Mapping("/adminPage/ssh")
@Controller
public class SshController extends BaseController {

	@Mapping("")
	public ModelAndView ssh(String id, String cmd) {

		ModelAndView modelAndView = new ModelAndView("/adminPage/ssh/index.html");
		modelAndView.put("id", id);
		modelAndView.put("cmd", cmd);
		return modelAndView;
	}
}
