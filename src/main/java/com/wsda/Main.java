package com.wsda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class Main {
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/greet")
    public GreetResponse greet(){
        return new GreetResponse("Hello", List.of("Java","Javascript"), new Person("Alex"));
    }

    record GreetResponse(
            String greet,
            List<String> programmingLanguages,
            Person person
    ){
    }

    record Person(String name){}
}
