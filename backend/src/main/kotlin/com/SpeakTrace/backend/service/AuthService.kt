package com.SpeakTrace.backend.service

import com.SpeakTrace.backend.model.User
import com.SpeakTrace.backend.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Service
class AuthService(
    private val userRepository: UserRepository
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun login(email: String, password: String): User? {
        val user = userRepository.findByEmail(email)
		System.out.println("Attempting login for email: $email")
        return if (user != null && passwordEncoder.matches(password, user.password)) {
            user
        } else {
            null
        }
    }

    fun signup(username: String, email: String, password: String): User? {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            return null
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