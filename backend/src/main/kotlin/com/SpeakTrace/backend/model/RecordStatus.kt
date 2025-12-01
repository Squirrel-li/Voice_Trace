package com.SpeakTrace.backend.model

enum class RecordStatus {
    PENDING,      // 修改為與資料庫一致
    PROCESSING,
    COMPLETED,
    FAILED
}