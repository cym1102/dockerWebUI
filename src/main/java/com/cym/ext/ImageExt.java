package com.cym.ext;

import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class ImageExt {
	private String imageName;
//	private Integer soraImage;
	
	/*--- 原始属性 --- */

    private Long created;

    private String id;

    private String parentId;

    private String[] repoTags;

    private String[] repoDigests;

    private Long size;

    private Long virtualSize;

    private Long sharedSize;

    public Map<String, String> labels;

    private Integer containers;

    public String getId() {
        return id;
    }

    public String[] getRepoTags() {
        return repoTags;
    }

    public String[] getRepoDigests() {
        return repoDigests;
    }

    public String getParentId() {
        return parentId;
    }

    public Long getCreated() {
        return created;
    }

    public Long getSize() {
        return size;
    }

    public Long getVirtualSize() {
        return virtualSize;
    }


    public Long getSharedSize() {
        return sharedSize;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public Integer getContainers() {
        return containers;
    }

	public void setCreated(Long created) {
		this.created = created;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setRepoTags(String[] repoTags) {
		this.repoTags = repoTags;
	}

	public void setRepoDigests(String[] repoDigests) {
		this.repoDigests = repoDigests;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setVirtualSize(Long virtualSize) {
		this.virtualSize = virtualSize;
	}

	public void setSharedSize(Long sharedSize) {
		this.sharedSize = sharedSize;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public void setContainers(Integer containers) {
		this.containers = containers;
	}

//	public Integer getSoraImage() {
//		return soraImage;
//	}
//
//	public void setSoraImage(Integer soraImage) {
//		this.soraImage = soraImage;
//	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
}
