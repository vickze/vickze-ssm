package io.vickze.validator;

import org.apache.commons.lang.StringUtils;
import io.vickze.exception.CheckException;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-08 22:07
 **/
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new CheckException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new CheckException(message);
        }
    }
}
