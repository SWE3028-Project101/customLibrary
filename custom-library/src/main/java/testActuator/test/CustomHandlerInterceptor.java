package testActuator.test;

import com.sun.management.ThreadMXBean;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.management.ManagementFactory;


public class CustomHandlerInterceptor implements HandlerInterceptor {

    private final MeterRegistry meterRegistry;


    public CustomHandlerInterceptor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 특정 URL로의 요청을 처리
        //meterRegistry.counter("custom.requests", "uri", request.getRequestURI(), "method", request.getMethod()).increment();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        System.out.println(executeTime);
        if (requestURI.equals("/")) {
            //System.out.println("Response Status: " + response.getStatus());
            // 요청을 처리한 후 메모리 사용량 추적

            // 응답 메트릭 카운터 증가
            //meterRegistry.counter("custom.responses", "uri", request.getRequestURI(), "status", String.valueOf(response.getStatus())).increment();

            // 요청을 처리한 후 메모리 사용량 추적
            long memoryUsage = analyzeMemoryUsage();

            meterRegistry.gauge("custom.memory.usage", Tags.of("uri",requestURI,"status",String.valueOf(response.getStatus()),"executingTime", String.valueOf(executeTime)+"ms"),memoryUsage);
        }
    }

    private long analyzeMemoryUsage() {
        ThreadMXBean threadMXBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        long threadId = Thread.currentThread().getId();
        long memoryUsage = threadMXBean.getThreadAllocatedBytes(threadId);
        //System.out.println("Thread ID: " + threadId + ", Memory Usage: " + memoryUsage);
        return memoryUsage;
    }
}

