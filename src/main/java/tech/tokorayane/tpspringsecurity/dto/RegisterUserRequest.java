package tech.tokorayane.tpspringsecurity.dto;

public record RegisterUserRequest(
        String email,
        String password
) {
}
