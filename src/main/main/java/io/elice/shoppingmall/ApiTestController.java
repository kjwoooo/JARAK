package io.elice.shoppingmall;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiTestController {

  @GetMapping("/api/test")
  public String hello(){
    return "테스트성공!";
  }
}
