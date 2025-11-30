package com.SpeakTrace.backend.controller

import com.SpeakTrace.backend.service.UploadService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import com.SpeakTrace.backend.util.JwtUtil

data class UserInfoRequest(
    val email: String,
    val username: String,
    val password: String
)

@RestController
@RequestMapping("/api/upload")
class UploadController(
    private val uploadService: UploadService,
    private val jwtUtil: JwtUtil
) {
    @PostMapping("/userinfo")
    fun uploadUserInfo(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody userInfo: UserInfoRequest
    ): ResponseEntity<Any> {
        return try {
            val token = authHeader.removePrefix("Bearer ").trim()
            val email = jwtUtil.extractEmail(token)
            val updated = uploadService.processUserInfo(email, userInfo)
            if (updated) {
                ResponseEntity.ok(mapOf("message" to "使用者資料更新", "token" to jwtUtil.generateToken(userInfo.email)))
            } else {
                ResponseEntity.badRequest().body("查無使用者")
            }
        }
        catch(e: Exception) {
                e.message?.let {
                return ResponseEntity.badRequest().body(it)
            }
            return ResponseEntity.badRequest().body("伺服器錯誤")
        }
    }
}

/* 
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/user `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"username":"testuser1","email":"testuser1@example.com","password":"$2a$10$7./PGdBzJrHVBazScrxM5.DImFybKslDQF6ICy7GnEOx.yIySqop."}'

Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/user `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"username":"testuser2","email":"testuser2@example.com","password":"$2a$10$7./PGdBzJrHVBazScrxM5.DImFybKslDQF6ICy7GnEOx.yIySqop."}'
*/