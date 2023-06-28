package com.cym.model;

import com.cym.sqlhelper.bean.BaseModel;
import com.cym.sqlhelper.config.InitValue;
import com.cym.sqlhelper.config.Table;

@Table
public class Cmd extends BaseModel {
	String cmdId;

	String line;

	String seq;
	
//	@InitValue("0")
//	Integer show;
//
//
//	public Integer getShow() {
//		return show;
//	}
//
//	public void setShow(Integer show) {
//		this.show = show;
//	}

	public String getCmdId() {
		return cmdId;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

}
