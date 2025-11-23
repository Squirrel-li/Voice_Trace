package com.SpeakTrace.backend.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import java.util.Base64
import java.io.File

@Component
object JwtUtil {
    private val SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private const val EXPIRATION = 86400000 // 1天

    init {
        // 確保資料夾存在
        val dir = File("secretKey")
        if (!dir.exists()) dir.mkdirs()
        // 存 base64 密鑰到檔案
        val base64Key = Base64.getEncoder().encodeToString(SECRET_KEY.encoded)
        File("secretKey/secret.key").writeText(base64Key)
    }

    fun generateToken(email: String): String {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(SECRET_KEY)
            .compact()
    }

    fun validateToken(token: String): String? {
        return try {
            Jwts.parser()
                .setSigningKey(SECRET_KEY) // 確保密鑰是 byte[]
                .parseClaimsJws(token)
                .body
                .subject
        } catch (e: Exception) {
            null
        }
    }
    
    fun extractEmail(token: String): String {
        val claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .body
        return claims.subject // 這裡的 subject 就是 email
    }

    fun getAuthentication(email: String): UsernamePasswordAuthenticationToken {
        val userDetails = User(email, "", listOf(SimpleGrantedAuthority("USER")))
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }
}