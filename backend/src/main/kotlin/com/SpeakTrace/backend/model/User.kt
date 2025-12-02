package com.SpeakTrace.backend.model

import jakarta.persistence.*
import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var username: String = "",

    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(nullable = false)
    var password: String = "",

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore
    val uploadRecords: MutableList<UploadRecord> = mutableListOf()
) {
    override fun toString(): String {
        return "User(id=$id, email='$email')"
    }
}