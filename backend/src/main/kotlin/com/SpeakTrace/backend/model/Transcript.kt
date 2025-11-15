package com.SpeakTrace.backend.model

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(name = "transcript")
data class Transcript(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UploadRecord_id", nullable = false)
    @JsonIgnore
    var uploadRecord: UploadRecord,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SpeakerInfo_id", nullable = false)
    var speakerInfo: SpeakerInfo,

    @Column(nullable = false)
    val textContent: String,

    @Column(nullable = false)
    val startTime: Double,

    @Column(nullable = false)
    val endTime: Double
)