package cn.lq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.lq.model.CommenResult;
import cn.lq.model.RedisClusterParam;
import cn.lq.service.RedisService;

@RestController
@RequestMapping("/redis/")
public class RedisController {

	@Autowired
	private RedisService redisService;

	@PostMapping("addRedisCluster")
	public CommenResult add(RedisClusterParam param) {
		return redisService.add(param);
	}

	@RequestMapping("getRedisList")
	public CommenResult getRedisList() {
		return redisService.getRedisList();
	}

	@RequestMapping("{name}/keys")
	public CommenResult keys(@PathVariable String name, String key) {
		return redisService.keys(name, key);
	}

	@RequestMapping("{name}/getValue")
	public CommenResult getValue(@PathVariable String name, String key) {
		return redisService.get(name, key);
	}

	@RequestMapping("{name}/deleteKey")
	public CommenResult deleteKey(@PathVariable String name, String key) {
		return redisService.delete(name, key);
	}

	@RequestMapping("{name}/batchDeleteKey")
	public CommenResult batchDeleteKey(@PathVariable String name, String key) {
		return redisService.batchDelete(name, key);
	}
}
