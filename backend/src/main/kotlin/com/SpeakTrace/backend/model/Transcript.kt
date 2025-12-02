package com.SpeakTrace.backend.model

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonBackReference

@Entity
@Table(name = "transcript")
data class Transcript(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_record_id", nullable = false)
    @JsonIgnore
    var uploadRecord: UploadRecord,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_info_id", nullable = false) // 修正欄位名稱
    var speakerInfo: SpeakerInfo,

    @Column(nullable = false)
    var textContent: String,

    @Column(nullable = false)
    val startTime: Double,

    @Column(nullable = false)
    val endTime: Double
) {
    override fun toString(): String {
        return "Transcript(id=$id, textContent='$textContent', startTime=$startTime, endTime=$endTime)"
    }
}