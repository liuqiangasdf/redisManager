package cn.lq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;

import cn.lq.RedisManagerApplication;

public class ApplicationStop implements ApplicationListener<ContextStoppedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisManagerApplication.class);

	public void onApplicationEvent(ContextStoppedEvent event) {
		LOGGER.info("执行ApplicationStop");
		LOGGER.info("执行ApplicationStop结束：" );
	}
}
