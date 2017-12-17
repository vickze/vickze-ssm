package io.vickze.exception;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2017-12-10 0:57
 */
@RestController
public class HttpErrorHandler implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public String handleError() {
        throw new CheckException("Not Found", 404);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
