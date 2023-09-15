package com.chocksaway.controller;

import com.chocksaway.entity.Employee;
import com.chocksaway.entity.Greeting;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ApiController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final Map<String, Employee> database = new HashMap<>();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/api/employees")
    Flux<Employee> employees() {
        return Flux.just( //
                new Employee("alice", "management"), //
                new Employee("bob", "payroll"));
    }

    @PostMapping("/api/employees")
    Mono<Employee> add(@RequestBody Mono<Employee> newEmployee) {
        return newEmployee
                .map(employee -> {
                    database.put(employee.name(), employee);
                    return employee;
                });
    }

    @GetMapping("/api/names")
    Flux<String> names() {
        Flux<String> a = Flux.just("alpha", "bravo");
        Flux<String> b = Flux.just("charlie", "delta");
        return a.mergeWith(b);
    }
}