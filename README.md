# CustomLibrary to get information about each thread
This library helps you to know each thread's memory usage, uri, executionTime, error, request time for Spring project.

# Getting Started
---

## Build.gradle Configuration
you need Spring Actuator as dependency

```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator:{version}'
}
```
## Here is a quick teaser of an application using customLibrary in Java:
```
@Configuration
@EnableScheduling
public class Config implements WebMvcConfigurer {

    private final MeterRegistry meterRegistry;

    @Autowired
    public Config(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // enroll interceptor
        registry.addInterceptor(new CustomHandlerInterceptor(meterRegistry));
    }

    @Scheduled(fixedRate = 5000)
    public void resetCustomCounter() {
        // reset counter
        meterRegistry.clear();
    }

}
```

