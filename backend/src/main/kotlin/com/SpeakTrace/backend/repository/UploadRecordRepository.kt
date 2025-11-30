package com.SpeakTrace.backend.repository

import com.SpeakTrace.backend.model.UploadRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UploadRecordRepository : JpaRepository<UploadRecord, Long> {
    fun findByUserId(userId: Long): List<UploadRecord>

    // 根據 User 的 email 查詢 UploadRecord
    fun findByUserEmail(email: String): List<UploadRecord>

    // 新增方法：根據 uploadRecordId 和 email 查詢 UploadRecord
    @Query("SELECT ur FROM UploadRecord ur WHERE ur.id = :uploadRecordId AND ur.user.email = :email")
    fun findByIdAndUserEmail(
        @Param("uploadRecordId") uploadRecordId: Long,
        @Param("email") email: String
    ): UploadRecord?
}