package com.cym.service;

import java.util.ArrayList;
import java.util.List;

import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.aspect.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.sqlhelper.utils.SqlHelper;
import com.cym.utils.LinuxTools;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import cn.hutool.core.util.StrUtil;

@Service
public class DockerService {
	Logger logger = LoggerFactory.getLogger(DockerService.class);

	@Inject
	SqlHelper sqlHelper;
	@Inject
	CmdService cmdService;

	DockerClient dockerClient;

	@Init
	public void getDockerClient() {
		if (!LinuxTools.isLinux()) {
			DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("tcp://localhost:2375").build();
			dockerClient = DockerClientBuilder.getInstance(config).build();
		} else {
			dockerClient = DockerClientBuilder.getInstance().build();
		}
		
	}

	public List<Container> searchContainer(String keywords) {
		try {
			List<Container> list = dockerClient.listContainersCmd().withShowAll(true).exec();
			if (StrUtil.isEmpty(keywords)) {
				return list;
			} else {
				List<Container> rsList = new ArrayList<Container>();
				top: for (Container container : list) {
					for (String name : container.getNames()) {
						if (name.contains(keywords)) {
							rsList.add(container);
							continue top;
						}
					}
				}

				return rsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Container>();
	}

	public List<Image> searchImage(String keywords) {
		try {

			List<Image> list = dockerClient.listImagesCmd().exec();
			if (StrUtil.isEmpty(keywords)) {
				return list;
			} else {
				List<Image> rsList = new ArrayList<Image>();
				top: for (Image image : list) {
					for (String name : image.getRepoTags()) {
						if (name.contains(keywords)) {
							rsList.add(image);
							continue top;
						}
					}
				}

				return rsList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Image>();
	}

	public List<SearchItem> searchOnlineImage(String imageName) {
		List<SearchItem> list = dockerClient.searchImagesCmd(imageName).exec();

		return list;
	}

	public void del(String id, String cmdId) {
		cmdService.run(cmdId, "docker rm -f " + id);
	}

	public void start(String id, String cmdId) {
		cmdService.run(cmdId, "docker start " + id);
	}

	public void stop(String id, String cmdId) {
		cmdService.run(cmdId, "docker stop " + id);
	}

	public void pullImageOver(String cmdId, String name) {
		cmdService.run(cmdId, "docker pull " + name);
	}

	public void delImage(String id, String cmdId) {
		cmdService.run(cmdId, "docker rmi -f " + id);
	}

	public void logs(String id, String cmdId) {
		cmdService.run(cmdId, "docker logs " + id);
	}

//	public Integer getSoraImage(String imageName) {
//
//		String[] myImages = new String[] { //
//				"mysql:8.0.28"//
//				, "mysql:5.7.37"//
//				, "mysql:5.6.51"//
//				, "mysql:5.5.59"//
//				, "cym1102/nginx-modsecurity:latest"//
//				, "cym1102/php-fpm:5.6"//
//				, "cym1102/php-fpm:7.4"//
//				, "cym1102/php-fpm:8.1"//
//				, "tomcat:9.0-jre8-temurin" //
//				, "tomcat:9.0-jre11-temurin" //
//				, "tomcat:9.0-jre17-temurin" //
//		};
//		for (String myImage : myImages) {
//			if (imageName.startsWith(myImage)) {
//				return 1;
//			}
//		}
//
//		return 0;
//	}

//	public void installDocker(String cmdId) {
//		if (LinuxTools.isApt()) {
//			cmdService.run(cmdId, "apt -y update");
//			cmdService.run(cmdId, "apt -y install docker.io");
//		} else if (LinuxTools.isYum()) {
//			cmdService.run(cmdId, "yum -y install docker");
//		}
//		cmdService.run(cmdId, "systemctl enable docker");
//		cmdService.run(cmdId, "systemctl start docker");
//	}

}
