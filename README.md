# CustomLibrary to get information about each thread
The primary goal of this project is to monitor the each of specific request being handled in thread pool of Spring project based on Tomcat Server. <p>
This library helps you to know each thread's memory usage, uri, executionTime, error, request time for Spring project.<p>
To ensure if it is working properly, check your uri : <p>


```
'{projects's host}:{project's port}/actuator/metrics/custom.memory.usage'

```

# Getting Started


### Build.gradle Configuration
you need Spring Actuator as dependency

#### Step 1. Add the JitPack repository to your build file

```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
#### Step 2. Add the dependency
```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator:{version}'
	implementation 'com.github.SWE3028-Project101:VisualizeThreadPool:Tag'
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

