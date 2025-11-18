package com.SpeakTrace.backend.service

import com.SpeakTrace.backend.repository.UserRepository
import com.SpeakTrace.backend.controller.UserInfoRequest
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder

data class UserInfoRequest(
    val email: String,
    val username: String,
    val password: String
)

@Service
class UploadService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun processUserInfo(originEmail: String, userInfo: UserInfoRequest): Boolean 
	{
		var user = userRepository.findByEmail(originEmail)
		if (user != null) 
		{
            val emailOwner = userRepository.findByEmail(userInfo.email)
            if (emailOwner != null && emailOwner.id != user.id) 
			{
                throw IllegalArgumentException("電子郵件 已存在")
            }
			val usernameOwner = userRepository.findByUsername(userInfo.username)
            if (usernameOwner != null && usernameOwner.id != user.id) 
			{
                throw IllegalArgumentException("使用者名稱 已存在")
            }
			if (userInfo.password.isNotBlank())
			{
				user.password = passwordEncoder.encode(userInfo.password)
			}
			user.username = userInfo.username
            user.email = userInfo.email
			userRepository.save(user)
			return true
		}
		else
		{
			return false
		}
    }
}