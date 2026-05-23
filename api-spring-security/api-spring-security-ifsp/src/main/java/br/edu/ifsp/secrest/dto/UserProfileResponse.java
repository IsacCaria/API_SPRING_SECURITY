package br.edu.ifsp.secrest.dto;

import java.util.List;

public record UserProfileResponse(
        Long id,
        String email,
        List<String> roles
) {
}
