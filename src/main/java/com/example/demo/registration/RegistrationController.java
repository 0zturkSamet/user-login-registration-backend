package com.example.demo.registration;

import com.example.demo.dto.RegistrationDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDto request) {
        String token = registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Registration successful! Please check your email to verify your account.",
                        "token", token
                ));
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return ResponseEntity.ok(Map.of(
                "message", "Email verified successfully! You can now login."
        ));
    }
}
