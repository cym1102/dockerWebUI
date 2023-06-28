package com.cym.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.noear.solon.annotation.Inject;
import org.noear.solon.aspect.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.model.Cmd;
import com.cym.sqlhelper.bean.Sort;
import com.cym.sqlhelper.bean.Sort.Direction;
import com.cym.sqlhelper.utils.ConditionAndWrapper;
import com.cym.sqlhelper.utils.SnowFlake;
import com.cym.sqlhelper.utils.SqlHelper;
import com.cym.utils.LinuxTools;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;

@Service
public class CmdService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	SqlHelper sqlHelper;

	SnowFlake snowFlake = new SnowFlake(1, 1);

	//Long pid = null;

	public boolean run(String cmdId, String cmdStr) {
		return run(cmdId, cmdStr, null);
	}

	public boolean run(String cmdId, String cmdStr, String[] envs) {
		// 不允许有双空格
		while (cmdStr.contains("  ")) {
			cmdStr = cmdStr.replace("  ", " ");
		}

		addLine("执行命令 > " + cmdStr, cmdId);
		boolean success = true;
		try {
			Process p = null;
			if (envs == null) {
				logger.info("开始执行");
				p = RuntimeUtil.exec(cmdStr);
			} else {
				p = RuntimeUtil.exec(envs, cmdStr);
			}
			//pid = p.pid();

			// 读取命令的输出信息
			logger.info("读取命令的输出信息");
			InputStream is = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			// 打印输出信息
			logger.info("打印输出信息开始");
			String line = null;
			while ((line = reader.readLine()) != null) {
				Cmd cmd = new Cmd();
				cmd.setCmdId(cmdId);
				cmd.setLine(line);
				cmd.setSeq(snowFlake.nextId());
				sqlHelper.insert(cmd);
				logger.info("输出信息：" + line);
			}
			logger.info("打印输出信息完成");
			
			// 检查命令是否执行失败。
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1) { // 0表示正常结束，1：非正常结束
					Cmd cmd = new Cmd();
					cmd.setCmdId(cmdId);
					cmd.setLine("命令执行失败!");
					cmd.setSeq(snowFlake.nextId());
					sqlHelper.insert(cmd);
					success = false;
				}
			}
			
			logger.info("命令执行完成");
			reader.close();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Cmd cmd = new Cmd();
			cmd.setCmdId(cmdId);
			cmd.setLine(e.getMessage());
			cmd.setSeq(snowFlake.nextId());
			sqlHelper.insert(cmd);
			success = false;
		}
		addLine("执行完成，可直接关闭窗口", cmdId);
		//pid = null;
		return success;
	}

	public void addLine(String line, String cmdId) {
		Cmd cmd = new Cmd();
		cmd.setCmdId(cmdId);
		cmd.setLine(line);
		cmd.setSeq(snowFlake.nextId());
		sqlHelper.insert(cmd);
		logger.info(line);

	}

	public String tail(String cmdId) {
		ConditionAndWrapper conditionAndWrapper = new ConditionAndWrapper().eq(Cmd::getCmdId, cmdId);
		List<Cmd> cmdList = sqlHelper.findListByQuery(conditionAndWrapper, new Sort(Cmd::getSeq, Direction.ASC), Cmd.class);
		List<String> list = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		for (Cmd cmd : cmdList) {
			String str = "<div><pre>" + cmd.getLine() + "</pre></div>";
			list.add(str);
			ids.add(cmd.getId());
		}
		if (ids.size() > 0) {
			sqlHelper.deleteByQuery(new ConditionAndWrapper().in(Cmd::getId, ids), Cmd.class);
		}

		return StrUtil.join("\n", list);
	}

//	public void kill() {
//		if (pid != null && LinuxTools.isLinux()) {
//			RuntimeUtil.exec("kill -9 " + pid);
//			logger.info("杀掉进程:" + pid);
//		}
//	}

}
