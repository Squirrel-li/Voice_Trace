package com.SpeakTrace.backend.service

import com.SpeakTrace.backend.model.User
import com.SpeakTrace.backend.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository
) {
    fun login(username: String, password: String): User? {
        val user = userRepository.findByUsername(username)
        // 實務上應用加密比對，這裡為簡化直接比對明文
        return if (user != null && user.password == password) {
            user
        } else {
            null
        }

	fun signup(username: String, email: String, password: String): User? {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            return null // 已存在相同帳號
        }
        val user = User(
            username = username,
            email = email,
            password = password,
            createdAt = LocalDateTime.now()
        )
        return userRepository.save(user)
    	}
	}
}