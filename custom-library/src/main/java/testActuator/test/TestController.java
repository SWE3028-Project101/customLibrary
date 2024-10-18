package testActuator.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    /*
    private final CustomHandlerInterceptor customHandlerInterceptor;

    @Autowired
    public TestController(CustomHandlerInterceptor customHandlerInterceptor) {
        this.customHandlerInterceptor = customHandlerInterceptor;
    }

     */

    @GetMapping("/")
    public String mainPage(){
        return "Hello World!";
    }
}
