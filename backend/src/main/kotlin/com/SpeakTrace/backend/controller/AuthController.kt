package com.SpeakTrace.backend.controller

import com.SpeakTrace.backend.model.User
import com.SpeakTrace.backend.service.AuthService
import org.springframework.web.bind.annotation.*
import com.SpeakTrace.backend.util.JwtUtil
import org.springframework.http.ResponseEntity
import com.SpeakTrace.backend.repository.UserRepository



@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val authService: AuthService
) {
    data class LoginRequest(val email: String, val password: String)
    data class SignupRequest(val username: String, val email: String, val password: String)

    @PostMapping("/login")
    fun login(@RequestBody body: LoginRequest): ResponseEntity<Any> {
        val token = authService.login(body.email, body.password)
        return if (token != null) {
            System.out.println("Login successful for email: ${body.email}")
            ResponseEntity.ok(mapOf("token" to token, "email" to body.email, "username" to userRepository.findByEmail(body.email)?.username))
        } else {
            ResponseEntity.badRequest().body(mapOf("message" to "無效的電子郵件或密碼"))
        }
    }

    @PostMapping("/signup")
    fun signup(@RequestBody body: SignupRequest): ResponseEntity<Any> {
        return try {
            val result = authService.signup(body.username, body.email, body.password)
            if (result == null) {
                throw IllegalArgumentException("伺服器錯誤")
            }
            ResponseEntity.ok(mapOf("username" to result.username))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("message" to e.message))
        }
    }
}