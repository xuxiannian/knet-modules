package cn.xxn.utils;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <b>ClassName:</b> JsonUtils <br>
 * <b>Reason:</b><br>
 * 
 * @version 1.0
 * @Date 2013-12-30 下午5:19:40
 * @since 1.0
 */

public class JsonUtils {

	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	public static String toJson(Object obj) {
		if (obj == null)
			return null;
		String json;
		try {
			json = mapper.writeValueAsString(obj);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return json;
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(String json, TypeReference<T> type) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return mapper.readValue(json, type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
