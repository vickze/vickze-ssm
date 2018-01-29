package io.vickze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2017-12-28 18:12
 */
@SpringBootApplication
@ImportResource("classpath:dubbo.xml")
public class BackgroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackgroundApplication.class, args);
    }
}
