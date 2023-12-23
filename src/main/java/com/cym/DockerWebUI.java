package com.cym;

import org.noear.solon.Solon;
import org.noear.solon.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnableScheduling
public class DockerWebUI {
	static Logger logger = LoggerFactory.getLogger(DockerWebUI.class);

	public static void main(String[] args) {
		Solon.start(DockerWebUI.class, args, app -> {
			app.enableWebSocket(true);
			app.enableWebSocketMvc(false);
			
			app.onError(e -> logger.error(e.getMessage(), e));

			app.before(c -> {
				String path = c.path();
				while (path.contains("//")) {
					path = path.replace("//", "/");
				}
				c.pathNew(path);
			});

			app.onEvent(freemarker.template.Configuration.class, cfg -> {
				cfg.setSetting("classic_compatible", "true");
				cfg.setSetting("number_format", "0.##");
			});
			
			app.router().caseSensitive(true);
		});
	}

}
