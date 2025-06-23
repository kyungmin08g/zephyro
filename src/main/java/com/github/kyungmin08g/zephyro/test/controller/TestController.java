package com.github.kyungmin08g.zephyro.test.controller;

import com.github.kyungmin08g.zephyro.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
@RequiredArgsConstructor
public class TestController {
  private final TestService testService;

  @GetMapping(value = "")
  public ResponseEntity<Void> test() {
    testService.print();
    return ResponseEntity.ok().build();
  }
}
