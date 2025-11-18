package com.SpeakTrace.backend.model

import jakarta.persistence.*
import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(name = "upload_record")
data class UploadRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val fileName: String,

    @Column(nullable = false)
    val filePath: String,
	
    @Column(nullable = false)
    val uploadedAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: RecordStatus = RecordStatus.PENDING,

    @OneToMany(mappedBy = "uploadRecord", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore
    var transcripts: MutableList<Transcript> = mutableListOf(),
)