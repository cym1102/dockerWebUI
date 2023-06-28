package com.cym.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

@Controller
@Mapping("/adminPage/tail")
public class TailController extends BaseController {
	Logger logger = LoggerFactory.getLogger(TailController.class);

	@Mapping("/tail")
	public ModelAndView index(ModelAndView modelAndView, String cmdId) {
		modelAndView.put("cmdId", cmdId);
		modelAndView.view("/adminPage/tail/index.html");
		return modelAndView;
	}

	@Mapping("/tailCmd")
	public JsonResult tailCmd(String cmdId) throws Exception {
		String rs = cmdService.tail(cmdId);
		return renderSuccess(rs);
	}
	
	
}
