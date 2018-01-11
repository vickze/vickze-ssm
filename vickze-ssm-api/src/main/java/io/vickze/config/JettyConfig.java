package io.vickze.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-01-11 17:11
 */
@Component
public class JettyConfig {
    @Value("${server.jetty.max-threads}")
    private int maxThreads;
    @Value("${server.jetty.min-threads}")
    private int minThreads;

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() {
        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
        factory.addServerCustomizers((JettyServerCustomizer) server -> {
            final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
            threadPool.setMaxThreads(maxThreads);
            threadPool.setMinThreads(minThreads);
        });
        return factory;
    }
}
