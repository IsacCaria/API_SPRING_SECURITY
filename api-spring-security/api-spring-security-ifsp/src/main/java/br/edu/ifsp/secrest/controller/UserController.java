package br.edu.ifsp.secrest.controller;

import br.edu.ifsp.secrest.dto.CreateUserRequest;
import br.edu.ifsp.secrest.dto.LoginRequest;
import br.edu.ifsp.secrest.dto.TokenResponse;
import br.edu.ifsp.secrest.dto.UserProfileResponse;
import br.edu.ifsp.secrest.security.AuthenticatedUser;
import br.edu.ifsp.secrest.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserAccountService userAccountService;

    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest request) {
        userAccountService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(userAccountService.login(request));
    }

    @GetMapping("/test")
    public ResponseEntity<String> testAuthentication() {
        return ResponseEntity.ok("Autenticado com sucesso!");
    }

    @GetMapping("/test/customer")
    public ResponseEntity<String> testCustomer() {
        return ResponseEntity.ok("Acesso de CUSTOMER autorizado!");
    }

    @GetMapping("/test/administrator")
    public ResponseEntity<String> testAdministrator() {
        return ResponseEntity.ok("Acesso de ADMINISTRATOR autorizado!");
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> currentUser(Authentication authentication) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(userAccountService.getProfile(authenticatedUser));
    }
}
