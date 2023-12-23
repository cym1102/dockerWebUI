package com.cym.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
@Mapping("/adminPage/image")
public class ImageController extends BaseController {
	@Inject
	DockerService dockerService;

	@Mapping("")
	public ModelAndView images(String keywords) {

		List<Image> list = dockerService.searchImage(keywords);
		List<ImageExt> extList = copyImageList(list);

		for (ImageExt imageExt : extList) {
			imageExt.setImageName(StrUtil.join(" ", Arrays.asList(imageExt.getRepoTags())));
		}

		ModelAndView modelAndView = new ModelAndView("/adminPage/image/index.html");
		modelAndView.put("keywords", keywords);
		modelAndView.put("list", extList);

		return modelAndView;
	}

	private List<ImageExt> copyImageList(List<Image> list) {
		List<ImageExt> extList = new ArrayList<ImageExt>();
		for (Image image : list) {
			ImageExt imageExt = new ImageExt();

			imageExt.setContainers(image.getContainers());
			imageExt.setCreated(image.getCreated());
			imageExt.setId(image.getId());
			imageExt.setLabels(image.getLabels());
			imageExt.setParentId(image.getParentId());
			imageExt.setRepoDigests(image.getRepoDigests());
			imageExt.setRepoTags(image.getRepoTags());
			imageExt.setSharedSize(image.getSharedSize());
			imageExt.setSize(image.getSize());
			imageExt.setVirtualSize(image.getVirtualSize());

			extList.add(imageExt);
		}

		return extList;
	}

	@Mapping("pullOver")
	public JsonResult pullImageOver(String cmdId, String name) {

		ThreadUtil.execAsync(new Runnable() {

			@Override
			public void run() {
				dockerService.pullImageOver(cmdId, name);
			}
		});

		return renderSuccess();
	}

	@Mapping("delImage")
	public JsonResult delImage(String id, String cmdId) {

		ThreadUtil.execAsync(new Runnable() {

			@Override
			public void run() {
				dockerService.delImage(id, cmdId);
			}
		});

		return renderSuccess();
	}

	@Mapping("searchImage")
	public JsonResult searchImage(String imageName) {

		List<SearchItem> list = dockerService.searchOnlineImage(imageName);

		return renderSuccess(list);
	}

}
