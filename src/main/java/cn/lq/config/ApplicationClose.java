package cn.lq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class ApplicationClose implements ApplicationListener<ContextClosedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationClose.class);

	public void onApplicationEvent(ContextClosedEvent event) {
		LOGGER.info("执行ApplicationClose");
		LOGGER.info("执行ApplicationClose结束：" );
	}
}
