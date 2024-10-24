package testActuator.test;

import com.sun.management.ThreadMXBean;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;


public class CustomHandlerInterceptor implements HandlerInterceptor {

    private final MeterRegistry meterRegistry;
    // private int requestNum = 0;
    private final AtomicInteger requestNum = new AtomicInteger(0); // Thread-safe counter

    private String requestURI;

    public CustomHandlerInterceptor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 특정 URL로의 요청을 처리
        long startTime = System.currentTimeMillis();
        requestURI = request.getRequestURI();
        //요청 객체에 새로운 속성 부여
        request.setAttribute("startTime", startTime);
        request.setAttribute("OriginalUri",requestURI);
        request.setAttribute("no error", false);
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       //에러 발생 안했으면 true로 부여
        request.setAttribute("no error", true);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        String isError = "";
        LocalDateTime currentTime = LocalDateTime.now();

        if( (Boolean) request.getAttribute("no error")) {
            isError = "no error";
            System.out.println(request.getRequestURI());
            System.out.println("no error");
        } else {
            isError = "error!";
            System.out.println(request.getRequestURI());
            System.out.println("error !");
        }
        if(!request.getRequestURI().equals("/error")) {
            Long memoryUsage = analyzeMemoryUsage();
            meterRegistry.counter("custom.requests", "uri", request.getRequestURI(), "requestNum", String.valueOf(requestNum.incrementAndGet()))
                    .increment();

            meterRegistry.gauge("custom.memory.usage", Tags.of(
                    "requestNum", String.valueOf(requestNum),
                    "memoryUsage", String.valueOf(memoryUsage),
                    "uri", request.getRequestURI() + "-" + requestNum,
                    "error", isError + "-" + requestNum,
                    "executingTime", String.valueOf(executeTime) + "ms",
                    "currentTime", String.valueOf(currentTime)), requestNum);

        }

    }

    private long analyzeMemoryUsage() {
        ThreadMXBean threadMXBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        long threadId = Thread.currentThread().getId();
        long memoryUsage = threadMXBean.getThreadAllocatedBytes(threadId);

        return memoryUsage;
    }

}


