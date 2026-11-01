package edu.example.labs;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/mock")
public class MockServices {
  @PostMapping("/credit")
  public Map<String,Object> credit(@RequestBody Map<String,Object> request) {
    if (Boolean.TRUE.equals(request.get("simulateError"))) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Synthetic credit API failure");
    double income = ((Number)request.getOrDefault("monthlyIncome",0)).doubleValue();
    int score = income >= 4000 ? 740 : income >= 2000 ? 650 : 500;
    return Map.of("score",score,"assessment","SYNTHETIC");
  }
  @PostMapping("/payment")
  public Map<String,Object> payment(@RequestBody Map<String,Object> request) {
    if (Boolean.TRUE.equals(request.get("simulateError"))) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Synthetic payment API failure");
    String token = String.valueOf(request.getOrDefault("paymentToken",""));
    return Map.of("status", token.equals("tok_decline") ? "DECLINED" : "PAID", "reference", "MOCK-PAY-001");
  }
  @PostMapping("/slots")
  public Map<String,Object> slots(@RequestBody Map<String,Object> request) {
    if (Boolean.TRUE.equals(request.get("simulateError"))) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Synthetic scheduling API failure");
    String preference = String.valueOf(request.getOrDefault("appointmentPreference",""));
    return Map.of("slots", preference.equals("none") ? List.of() : List.of("2026-11-05T10:00:00+05:30"));
  }
}
