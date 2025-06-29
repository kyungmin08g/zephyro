package com.github.kyungmin08g.zephyro.test.controller;

import com.github.kyungmin08g.zephyro.query.annotation.QueryLogTarget;
import com.github.kyungmin08g.zephyro.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
@RequiredArgsConstructor
@QueryLogTarget
public class TestController {
  private final TestService testService;

  @GetMapping(value = "")
  public ResponseEntity<Void> test() {
    testService.testRunTimer();
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/create")
  public ResponseEntity<Void> create() {
    testService.createTest();
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/get/{id}")
  public ResponseEntity<Void> getTestById(@PathVariable("id") String id) {
    testService.getTestById(id);
    return ResponseEntity.ok().build();
  }
}
