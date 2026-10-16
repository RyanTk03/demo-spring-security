package tech.tokorayane.tpspringsecurity.dto;

public record LoginUserRequest(
        String email,
        String password
) {
}
