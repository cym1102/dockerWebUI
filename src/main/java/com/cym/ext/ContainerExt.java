package com.cym.ext;

import java.util.List;
import java.util.Map;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerHostConfig;
import com.github.dockerjava.api.model.ContainerMount;
import com.github.dockerjava.api.model.ContainerNetworkSettings;
import com.github.dockerjava.api.model.ContainerPort;

public class ContainerExt {

	private String nameStr;
	private String portStr;
	private Integer soraContainer;
	private Integer run;
	
	/*--- 原始属性 --- */

	private String command;

	private Long created;

	private String id;

	private String image;

	private String imageId;

	private String[] names;

	public ContainerPort[] ports;

	public Map<String, String> labels;

	private String status;

	private String state;

	private Long sizeRw;

	private Long sizeRootFs;

	private ContainerHostConfig hostConfig;

	private ContainerNetworkSettings networkSettings;

	private List<ContainerMount> mounts;

	public String getId() {
		return id;
	}

	public String getCommand() {
		return command;
	}

	public String getImage() {
		return image;
	}

	public String getImageId() {
		return imageId;
	}

	public Long getCreated() {
		return created;
	}

	public String getStatus() {
		return status;
	}

	public String getState() {
		return state;
	}

	public ContainerPort[] getPorts() {
		return ports;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public String[] getNames() {
		return names;
	}

	/**
	 * @see #sizeRw
	 */

	public Long getSizeRw() {
		return sizeRw;
	}

	/**
	 * @see #sizeRootFs
	 */

	public Long getSizeRootFs() {
		return sizeRootFs;
	}

	/**
	 * @see #networkSettings
	 */

	public ContainerNetworkSettings getNetworkSettings() {
		return networkSettings;
	}

	/**
	 * @see #hostConfig
	 */
	public ContainerHostConfig getHostConfig() {
		return hostConfig;
	}

	public List<ContainerMount> getMounts() {
		return mounts;
	}

	public Integer getSoraContainer() {
		return soraContainer;
	}

	public void setSoraContainer(Integer soraContainer) {
		this.soraContainer = soraContainer;
	}

	public String getNameStr() {
		return nameStr;
	}

	public void setNameStr(String nameStr) {
		this.nameStr = nameStr;
	}

	public String getPortStr() {
		return portStr;
	}

	public void setPortStr(String portStr) {
		this.portStr = portStr;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public void setPorts(ContainerPort[] ports) {
		this.ports = ports;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setSizeRw(Long sizeRw) {
		this.sizeRw = sizeRw;
	}

	public void setSizeRootFs(Long sizeRootFs) {
		this.sizeRootFs = sizeRootFs;
	}

	public void setHostConfig(ContainerHostConfig hostConfig) {
		this.hostConfig = hostConfig;
	}

	public void setNetworkSettings(ContainerNetworkSettings networkSettings) {
		this.networkSettings = networkSettings;
	}

	public void setMounts(List<ContainerMount> mounts) {
		this.mounts = mounts;
	}

	public Integer getRun() {
		return run;
	}

	public void setRun(Integer run) {
		this.run = run;
	}

}
