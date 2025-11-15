package com.SpeakTrace.backend.repository

import com.SpeakTrace.backend.model.Transcript
import org.springframework.data.jpa.repository.JpaRepository

interface TranscriptRepository : JpaRepository<Transcript, Long> {
    fun findByUploadRecordId(record_id: Long): Transcript?
}