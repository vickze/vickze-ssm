package io.vickze.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-08 22:07
 */
public class ResultDO extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public ResultDO() {
		put("code", 0);
		put("success", true);
	}
	
	public static ResultDO error() {
		return error(1, "服务器出了点小问题~");
	}
	
	public static ResultDO error(String msg) {
		return error(1, msg);
	}
	
	public static ResultDO error(int code, String msg) {
		ResultDO r = new ResultDO();
		r.put("code", code);
		r.put("msg", msg);
		r.put("success", false);
		return r;
	}

	public static ResultDO success(String msg) {
		ResultDO r = new ResultDO();
		r.put("msg", msg);
		return r;
	}
	
	public static ResultDO success(Map<String, Object> map) {
		ResultDO r = new ResultDO();
		r.putAll(map);
		return r;
	}
	
	public static ResultDO success() {
		return new ResultDO();
	}

	public ResultDO put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
