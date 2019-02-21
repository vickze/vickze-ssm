package io.vickze.validator;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import io.vickze.entity.BaseDO;
import io.vickze.exception.CheckException;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-08 22:31
 */
public class ValidatorUtil {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws CheckException 校验不通过，则报CheckException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws CheckException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new CheckException(constraintViolations.iterator().next().getMessage());
        }
    }

    /**
     * 校验对象
     *
     * @param collection 待校验对象集合
     * @param groups     待校验的组
     * @throws CheckException 校验不通过，则报CheckException异常
     */
    public static void validateCollection(Collection<? extends Serializable> collection, Class<?>... groups)
            throws CheckException {
        for (Object object : collection) {
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
            if (!constraintViolations.isEmpty()) {
                throw new CheckException(constraintViolations.iterator().next().getMessage());
            }
        }
    }
}
