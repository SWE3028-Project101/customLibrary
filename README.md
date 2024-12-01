# CustomLibrary to get information about each thread
The primary goal of this project is to monitor the each of specific request being handled in thread pool of Spring project based on Tomcat Server.
This library helps you to know each thread's memory usage, uri, executionTime, error, request time for Spring project.
To ensure if it is working properly, check your uri : <span style ="color:#808080"> '{projects's host}:{project's port}/actuator/metrics/custom.memory.usage'</span>


# Getting Started


### Build.gradle Configuration
you need Spring Actuator as dependency

```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator:{version}'
}
```
### Here is a quick teaser of an application using customLibrary in Java:
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

