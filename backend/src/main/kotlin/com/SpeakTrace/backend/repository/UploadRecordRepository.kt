package com.SpeakTrace.backend.repository

import com.SpeakTrace.backend.model.UploadRecord
import org.springframework.data.jpa.repository.JpaRepository

interface UploadRecordRepository : JpaRepository<UploadRecord, Long> {
    fun findByUserId(userId: Long): List<UploadRecord>

    // 新增方法：根據 User 的 email 查詢 UploadRecord
    fun findByUserEmail(email: String): List<UploadRecord>
}