package com.cym.controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.ServerEndpoint;
import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.message.Listener;
import org.noear.solon.core.message.Message;
import org.noear.solon.core.message.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.service.SshService;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @author noear 2021/4/22 created
 */
@ServerEndpoint(value = "/terminal")
public class SshWebSocket implements Listener {

	Logger logger = LoggerFactory.getLogger(SshWebSocket.class);

	Map<String, SshService> sshMap = new ConcurrentHashMap<>();

	private SshService getFromMap(Session session) {
		SshService sshService = sshMap.get(session.sessionId());

		if (sshService == null) {
			sshService = new SshService();
			sshMap.put(session.sessionId(), sshService);
		}

		return sshService;
	}

	@Override
	public void onOpen(Session session) {
		logger.info("用户[ {} ]成功连接!", session.sessionId());

		SshService sshService = getFromMap(session);
		// 初始化连接信息
		sshService.setWebSocketSession(session);
	}

	@Override
	public void onMessage(Session session, Message message) throws IOException {
		logger.info("用户[ {} ]发送命令: {}", session.sessionId(), message.toString());

		JSONObject messageMap = JSONUtil.parseObj(message.bodyAsString());
		SshService sshService = getFromMap(session);
		
		if (messageMap.containsKey("type")) {
			String type = messageMap.getStr("type");
			try {
				switch (type) {
				case "TERMINAL_INIT":
					sshService.onTerminalInit();
					break;
				case "TERMINAL_READY":
					sshService.onTerminalReady(messageMap.getStr("id"), messageMap.getStr("cmd"));
					break;
				case "TERMINAL_COMMAND":
					sshService.onCommand(messageMap.getStr("command"));
					break;
				case "TERMINAL_RESIZE":
					sshService.onTerminalResize(messageMap.getStr("columns"), messageMap.getStr("rows"));
					break;
				default:
					throw new RuntimeException("Unrecodnized action");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onError(Session session, Throwable exception) {
		logger.error("用户[ {} ]出现错误", session.sessionId(), exception);
	}

	@Override
	public void onClose(Session session) {
		logger.info("用户[ {} ]已断开连接", session.sessionId());
		
		
		// 关闭连接
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			SshService sshService = getFromMap(session);
			sshService.onTerminalClose();
			sshMap.remove(session.sessionId());
		}
	}

}
