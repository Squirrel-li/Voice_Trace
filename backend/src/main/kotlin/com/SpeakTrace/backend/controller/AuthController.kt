package com.SpeakTrace.backend.controller

import com.SpeakTrace.backend.model.User
import com.SpeakTrace.backend.service.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    data class LoginRequest(val email: String, val password: String)
    data class SignupRequest(val username: String, val email: String, val password: String)

    @PostMapping("/login")
    fun login(@RequestBody body: LoginRequest): User? {
        return authService.login(body.email, body.password)
    }

    @PostMapping("/signup")
    fun signup(@RequestBody body: SignupRequest): User? {
        return authService.signup(body.username, body.email, body.password)
    }
}