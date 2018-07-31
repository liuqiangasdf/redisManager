package cn.lq.model;

public class RedisClusterParam {

	private String name;
	private String clusterNodes;
	private String password;

	public RedisClusterParam() {
	}

	public RedisClusterParam(String clusterNodes) {
		super();
		this.clusterNodes = clusterNodes;
		this.password = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "[clusterNodes=" + clusterNodes + ", password=" + password + "]";
	}

}
