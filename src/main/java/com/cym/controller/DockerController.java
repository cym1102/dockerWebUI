package com.cym.controller;

import java.util.ArrayList;
import java.util.List;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

import com.cym.ext.ContainerExt;
import com.cym.ext.ImageExt;
import com.cym.service.DockerService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;

@Controller
@Mapping("/adminPage/docker")
public class DockerController extends BaseController {
	@Inject
	DockerService dockerService;

	@Mapping("")
	public ModelAndView index(String keywords) {
		List<Container> list = dockerService.searchContainer(keywords);

		List<ContainerExt> extList = copyList(list);
		for (ContainerExt containerExt : extList) {
			containerExt.setNameStr(StrUtil.join(" ", containerExt.getNames()));
			containerExt.setPortStr(StrUtil.join("<br>", containerExt.getPorts()));
			if (containerExt.getNameStr().contains("sora.")) {
				containerExt.setSoraContainer(1);
			} else {
				containerExt.setSoraContainer(0);
			}

			containerExt.setRun(containerExt.getStatus().contains("Exited") ? 0 : 1);
		}

		ModelAndView modelAndView = new ModelAndView("/adminPage/docker/index.html");
		modelAndView.put("keywords", keywords);
		modelAndView.put("list", extList);

		return modelAndView;
	}

	private List<ContainerExt> copyList(List<Container> list) {
		List<ContainerExt> extList = new ArrayList<ContainerExt>();
		for (Container container : list) {
			ContainerExt containerExt = new ContainerExt();
			containerExt.setCommand(container.getCommand());
			containerExt.setCreated(container.getCreated());
			containerExt.setHostConfig(container.getHostConfig());
			containerExt.setId(container.getId());
			containerExt.setImage(container.getImage());
			containerExt.setImageId(container.getImageId());
			containerExt.setLabels(container.getLabels());
			containerExt.setMounts(container.getMounts());
			containerExt.setNames(container.getNames());
			containerExt.setNetworkSettings(container.getNetworkSettings());
			containerExt.setPorts(container.getPorts());
			containerExt.setSizeRootFs(container.getSizeRootFs());
			containerExt.setSizeRw(container.getSizeRw());
			containerExt.setState(container.getState());
			containerExt.setStatus(container.getStatus());

			extList.add(containerExt);
		}

		return extList;
	}



	@Mapping("addOver")
	public JsonResult addOver(String cmdId, String dockerCmd) {

		ThreadUtil.execAsync(new Runnable() {

			@Override
			public void run() {
				cmdService.run(cmdId, dockerCmd);
			}
		});

		return renderSuccess();
	}


	@Mapping("logs")
	public JsonResult logs(String id, String cmdId) {

		ThreadUtil.execAsync(new Runnable() {

			@Override
			public void run() {
				dockerService.logs(id, cmdId);
			}
		});

		return renderSuccess();
	}

	@Mapping("del")
	public JsonResult del(String id, String cmdId) {

		ThreadUtil.execAsync(new Runnable() {

			@Override
			public void run() {
				dockerService.del(id, cmdId);
			}
		});

		return renderSuccess();
	}

	@Mapping("start")
	public JsonResult start(String id, String cmdId) {
		ThreadUtil.execAsync(new Runnable() {

			@Override
			public void run() {
				dockerService.start(id, cmdId);
			}
		});

		return renderSuccess();
	}

	@Mapping("stop")
	public JsonResult stop(String id, String cmdId) {
		ThreadUtil.execAsync(new Runnable() {

			@Override
			public void run() {
				dockerService.stop(id, cmdId);
			}
		});

		return renderSuccess();
	}

	@Mapping("getAllImages")
	public JsonResult getAllImages() {

		List<Image> list = dockerService.searchImage(null);

		return renderSuccess(list);
	}
	
//	@Mapping("installDocker")
//	public JsonResult installDocker(String cmdId) {
//		ThreadUtil.execAsync(new Runnable() {
//
//			@Override
//			public void run() {
//				dockerService.installDocker(cmdId);
//			}
//		});
//
//		return renderSuccess();
//	}
}
