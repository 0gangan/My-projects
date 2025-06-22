package lzg.example.formalSecurityTest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
@Configuration
public class ExecutorConfiguration {
    @Bean
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
// 核心线程
        executor.setCorePoolSize(5);
// 最大线程
        executor.setMaxPoolSize(10);
// 队列容量
        executor.setQueueCapacity(1000);
// 保持时间（默认秒）
        executor.setKeepAliveSeconds(5);
// 线程名称前缀
        executor.setThreadNamePrefix("test-thread-");
// 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}