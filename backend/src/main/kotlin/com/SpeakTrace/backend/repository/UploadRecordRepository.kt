package com.SpeakTrace.backend.repository

import com.SpeakTrace.backend.model.UploadRecord
import org.springframework.data.jpa.repository.JpaRepository

interface UploadRecordRepository : JpaRepository<UploadRecord, Long> {
    fun findByUserId(userId: Long): List<UploadRecord>
}