package pl.ibart.springboot.logicalBasePath.business;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/words")
public class SophisticateController {

    @GetMapping
    public String sayHello(){
        return "Hello you.";
    }

    @PostMapping("/sayMyName")
    public String sayMyName(@RequestBody String name){
        return "Hello." + name;
    }
}
