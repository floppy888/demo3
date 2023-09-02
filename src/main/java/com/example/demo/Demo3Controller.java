package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo3Controller {
    @GetMapping("/helloWorld")
    public String helloWorld(){
        return "Hello World!";
    }
}

