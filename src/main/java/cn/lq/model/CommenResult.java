package cn.lq.model;

public class CommenResult {

	public static final int SUCCESS = 1;
	public static final int FAIL = 1;

	private int code;
	private String msg;
	private long count;
	private Object data;

	public CommenResult() {
	}

	public CommenResult(int code) {
		super();
		this.code = code;
		this.msg = "SUCCESS";
	}
	
	public CommenResult(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public CommenResult(int code, long count) {
		super();
		this.code = code;
		this.count = count;
		this.msg = "SUCCESS";
	}

	public CommenResult(int code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "[code=" + code + ", msg=" + msg + ", count=" + count + ", data=" + data + "]";
	}

	
}
