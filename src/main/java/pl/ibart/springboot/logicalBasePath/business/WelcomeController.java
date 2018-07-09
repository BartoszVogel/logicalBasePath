package pl.ibart.springboot.logicalBasePath.business;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.Map;

@Controller
public class WelcomeController {

    @Value("${application.message:Hello World}")
    private String message;

    @Value("${logical.server.context-path:}")
    private String logicalBasePath;

    @GetMapping({"/", ""})
    public String welcome(Map<String, Object> model) {
        model.put("time", Instant.now());
        model.put("message", this.message);
        model.put("rootContext", this.logicalBasePath);
        return "welcome";
    }

    @GetMapping("/foo")
    public String foo(Map<String, Object> model) {
        throw new RuntimeException("Foo");
    }
}
