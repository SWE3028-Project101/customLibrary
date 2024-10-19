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
import java.time.LocalDateTime;


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
        LocalDateTime currentTime = LocalDateTime.now();
        //System.out.println(executeTime);
        if (requestURI.equals("/")) {

            long memoryUsage = analyzeMemoryUsage();

            meterRegistry.gauge("custom.memory.usage", Tags.of("uri",requestURI,"status",String.valueOf(response.getStatus()),"executingTime", String.valueOf(executeTime)+"ms","currentTime",String.valueOf(currentTime)),memoryUsage);
        }
    }

    private long analyzeMemoryUsage() {
        ThreadMXBean threadMXBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        long threadId = Thread.currentThread().getId();
        long memoryUsage = threadMXBean.getThreadAllocatedBytes(threadId);

        return memoryUsage;
    }
}

