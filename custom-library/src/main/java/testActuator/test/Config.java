package testActuator.test;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    private final MeterRegistry meterRegistry;

    @Autowired
    public Config(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인터셉터 등록 및 특정 경로에만 적용
        registry.addInterceptor(new CustomHandlerInterceptor(meterRegistry));
    }
    @Scheduled(fixedRate = 10000)
    public void resetCustomCounter() {
        // 카운터를 수동으로 리셋
        meterRegistry.clear();
        System.out.println("Custom counter has been reset.");
    }

}
