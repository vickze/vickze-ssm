package io.vickze.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json处理工具类
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-08 22:31
 **/
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();

    public static<T> String toJson(T data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (Exception e) {
            logger.error("To json failure, data:{}", data);
            return null;
        }
    }

    public static<T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("From json failure, json:{}", json);
            return null;
        }
    }

    public static<T> T fromJson(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            logger.error("From json failure, json:{}", json);
            return null;
        }
    }
}
