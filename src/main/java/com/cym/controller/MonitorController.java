package com.cym.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.ext.MonitorInfo;
import com.cym.ext.NetworkInfo;
import com.cym.service.MonitorService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.NetWorkUtil;

@Mapping("/adminPage/monitor")
@Controller
public class MonitorController extends BaseController {
	@Inject
	MonitorService monitorService;


	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Mapping("")
	public ModelAndView index(ModelAndView modelAndView) {

		modelAndView.put("list", monitorService.getDiskInfo());

		
		modelAndView.view("/adminPage/monitor/index.html");
		return modelAndView;
	}

	@Mapping("check")
	public JsonResult check() {

		MonitorInfo monitorInfo = monitorService.getMonitorInfoOshi();

		return renderSuccess(monitorInfo);
	}

	@Mapping("network")
	public JsonResult network() {
		NetworkInfo networkInfo = NetWorkUtil.getNetworkDownUp();
		return renderSuccess(networkInfo);
	}


}
