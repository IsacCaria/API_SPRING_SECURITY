package br.edu.ifsp.secrest.service;

import br.edu.ifsp.secrest.domain.AppRole;
import br.edu.ifsp.secrest.domain.AppUser;
import br.edu.ifsp.secrest.dto.CreateUserRequest;
import br.edu.ifsp.secrest.dto.LoginRequest;
import br.edu.ifsp.secrest.dto.TokenResponse;
import br.edu.ifsp.secrest.dto.UserProfileResponse;
import br.edu.ifsp.secrest.repository.UserRepository;
import br.edu.ifsp.secrest.security.AuthenticatedUser;
import br.edu.ifsp.secrest.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class UserAccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserAccountService(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              AuthenticationManager authenticationManager,
                              JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(CreateUserRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este email");
        }

        var encryptedPassword = passwordEncoder.encode(request.password());
        var role = new AppRole(request.role());
        var user = new AppUser(email, encryptedPassword, new LinkedHashSet<>(Set.of(role)));

        userRepository.save(user);
    }

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        String token = jwtService.createToken(authenticatedUser);

        return new TokenResponse(token);
    }

    public UserProfileResponse getProfile(AuthenticatedUser authenticatedUser) {
        AppUser user = authenticatedUser.getUser();

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                authenticatedUser.getRoleNames()
        );
    }
}
