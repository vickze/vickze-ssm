package io.vickze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2017-12-28 18:12
 */
@SpringBootApplication
@ImportResource("classpath:dubbo.xml")
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
