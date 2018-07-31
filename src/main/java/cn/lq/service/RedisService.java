package cn.lq.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;

import cn.lq.model.CommenResult;
import cn.lq.model.RedisClusterParam;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisService.class);
	private static ConcurrentHashMap<String, StringRedisTemplate> redisTemplateMap = new ConcurrentHashMap<>();

	@Autowired
	private JedisPoolConfig jedisPoolConfig;

	/**
	 * 添加redis集群
	 * 
	 * @param name
	 * @param nodeStr
	 * @return
	 */
	public CommenResult add(RedisClusterParam param) {
		CommenResult result = null;
		Set<String> setNodes = new HashSet<>();
		if (redisTemplateMap.containsKey(param.getName())) {
			result = new CommenResult(CommenResult.FAIL, "请勿重复添加");
			return result;
		}
		String nodeStr = param.getClusterNodes();
		if (StringUtils.isBlank(nodeStr)) {
			result = new CommenResult(CommenResult.FAIL, "节点不能为空");
			return result;
		}
		String[] nodes = nodeStr.split(",");
		for (String node : nodes) {
			if (StringUtils.isBlank(node)) {
				continue;
			}
			setNodes.add(node.trim());
		}
		if (CollectionUtils.isEmpty(setNodes)) {
			result = new CommenResult(CommenResult.FAIL, "节点不能为空");
			return result;
		}
		RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(setNodes);
		clusterConfig.setMaxRedirects(5);
		if (StringUtils.isNotBlank(param.getPassword())) {
			clusterConfig.setPassword(RedisPassword.of(param.getPassword()));
		}
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(clusterConfig, jedisPoolConfig);
		connectionFactory.afterPropertiesSet();

		StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
		List<RedisClientInfo> clientInfos = template.getClientList();
		if (CollectionUtils.isEmpty(clientInfos)) {
			result = new CommenResult(CommenResult.FAIL, "连接失败");
			return result;
		}
		redisTemplateMap.put(param.getName(), template);
		result = new CommenResult(CommenResult.SUCCESS);
		return result;
	}

	/**
	 * 获取redis列表
	 * 
	 * @return
	 */
	public CommenResult getRedisList() {
		Set<String> keys = redisTemplateMap.keySet();
		return new CommenResult(CommenResult.SUCCESS, "获取列表成功", keys);
	}

	/**
	 * 获取KRY列表
	 * 
	 * @param name
	 * @param key
	 * @return
	 */
	public CommenResult keys(String name, String key) {
		CommenResult result = null;
		result = this.checkTemplate(name);
		if (result.getCode() != CommenResult.SUCCESS) {
			return result;
		}
		StringRedisTemplate template = redisTemplateMap.get(name);
		Set<String> keys = new HashSet<>();

		if (StringUtils.isNotBlank(key)) {
			keys = template.keys("*" + key + "*");
		}
		result = new CommenResult(CommenResult.SUCCESS, "获取列表成功", keys);
		return result;
	}

	public CommenResult get(String name, String key) {
		CommenResult result = null;
		result = this.checkTemplate(name);
		if (result.getCode() != CommenResult.SUCCESS) {
			return result;
		}
		StringRedisTemplate template = redisTemplateMap.get(name);
		String value = null;
		if (StringUtils.isNotBlank(key)) {
			value = template.boundValueOps(key).get();
		}
		result = new CommenResult(CommenResult.SUCCESS, value);
		return result;
	}

	/**
	 * 删除KEY
	 * 
	 * @param name
	 * @param key
	 * @return
	 */
	public CommenResult delete(String name, String key) {
		CommenResult result = null;
		result = this.checkTemplate(name);
		if (result.getCode() != CommenResult.SUCCESS) {
			return result;
		}
		StringRedisTemplate template = redisTemplateMap.get(name);
		boolean count = template.delete(key);
		result = new CommenResult(CommenResult.SUCCESS, count ? 1 : 0);
		return result;
	}

	/**
	 * 批量删除KEY
	 * 
	 * @param name
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public CommenResult batchDelete(String name, String key) {
		CommenResult result = this.keys(name, key);
		if (result.getCode() == CommenResult.SUCCESS) {
			Set<String> keys = (Set<String>) result.getData();
			if (CollectionUtils.isEmpty(keys)) {
				result = new CommenResult(CommenResult.SUCCESS, 0);
				return result;
			}
			StringRedisTemplate template = redisTemplateMap.get(name);
			long count = template.delete(keys);
			result = new CommenResult(CommenResult.SUCCESS, count);
		} else {
			result = new CommenResult(CommenResult.FAIL, "删除失败");
		}
		return result;
	}

	/**
	 * redis配置缓存校验
	 * 
	 * @param name
	 * @return
	 */
	private CommenResult checkTemplate(String name) {
		CommenResult result = null;
		if (!redisTemplateMap.containsKey(name)) {
			result = new CommenResult(CommenResult.FAIL, "没有对应的redis配置");
			return result;
		}
		result = new CommenResult(CommenResult.SUCCESS);
		return result;
	}

	/**
	 * redis配置缓存初始化
	 * 
	 * @param map
	 * @return
	 */
	public CommenResult initTemplate(Map<String, RedisClusterParam> map) {
		CommenResult result = new CommenResult(CommenResult.SUCCESS);
		if (CollectionUtils.isEmpty(map)) {
			return result;
		}

		for (String name : map.keySet()) {
			if (redisTemplateMap.containsKey(name)) {
				continue;
			}
			RedisClusterParam param = map.get(name);
			String nodeStr = param.getClusterNodes();
			if (StringUtils.isBlank(nodeStr)) {
				continue;
			}
			String[] nodes = nodeStr.split(",");
			Set<String> setNodes = new HashSet<>();
			for (String node : nodes) {
				if (StringUtils.isBlank(node)) {
					continue;
				}
				setNodes.add(node.trim());
			}
			if (CollectionUtils.isEmpty(setNodes)) {
				continue;
			}
			RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(setNodes);
			clusterConfig.setMaxRedirects(5);
			JedisConnectionFactory connectionFactory = new JedisConnectionFactory(clusterConfig, jedisPoolConfig);
			connectionFactory.afterPropertiesSet();
			StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
			List<RedisClientInfo> list = template.getClientList();
			LOGGER.info(JSONObject.toJSONString(list));
			redisTemplateMap.put(name, template);
		}
		result.setData(redisTemplateMap.size());
		return result;
	}

}
