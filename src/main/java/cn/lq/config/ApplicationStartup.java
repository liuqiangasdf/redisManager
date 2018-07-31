package cn.lq.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import cn.lq.RedisManagerApplication;
import cn.lq.model.CommenResult;
import cn.lq.model.RedisClusterParam;
import cn.lq.service.RedisService;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisManagerApplication.class);

	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 在容器加载完毕后获取配置文件中的配置
		LOGGER.info("执行ApplicationStartup");
		ApplicationContext ac = event.getApplicationContext();
		RedisService redisService = ac.getBean(RedisService.class);
		RedisClusterProperties clusterProperties = ac.getBean(RedisClusterProperties.class);
		Map<String, String> nodes = clusterProperties.getClusterNodes();
		Map<String, RedisClusterParam> map = new HashMap<>();
		if (CollectionUtils.isEmpty(nodes)) {
			LOGGER.info("没有初始化的redis配置");
			return;
		}
		for (String nodeStr : nodes.keySet()) {
			String nodeValue = nodes.get(nodeStr);
			map.put(nodeStr, new RedisClusterParam(nodeValue));
		}
		CommenResult result = redisService.initTemplate(map);
		LOGGER.info("执行ApplicationStartup结束：" + result);
	}
}
