package ir.mohaymen.iris.demo;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")

public class DemoController {


  @GetMapping
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello from secured endpoint");
  }

  @GetMapping("/asfa")
  public ResponseEntity<String> pass() {
    return ResponseEntity.ok(passwordEncoder.encode("password"));
  }
  @Autowired
  private PasswordEncoder passwordEncoder;

}
