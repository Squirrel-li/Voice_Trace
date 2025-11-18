package com.SpeakTrace.backend.service

import com.SpeakTrace.backend.model.User
import com.SpeakTrace.backend.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import com.SpeakTrace.backend.util.JwtUtil

data class LoginResult(val user: User, val token: String)

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    fun login(email: String, password: String): String? {
        val user = userRepository.findByEmail(email)
        if (user != null && passwordEncoder.matches(password, user.password)) {
            return JwtUtil.generateToken(email)
        }
        return null
    }

    fun signup(username: String, email: String, password: String): User? {
        if (userRepository.existsByUsername(username)) {
            throw IllegalArgumentException("使用者名稱已存在")
        }
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("Email 已存在")
        }
        val user = User(
            username = username,
            email = email,
            password = passwordEncoder.encode(password), // 密碼加密儲存
            createdAt = LocalDateTime.now()
        )
        return userRepository.save(user)
    }
}