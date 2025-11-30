package com.SpeakTrace.backend.service

import com.SpeakTrace.backend.model.UploadRecord
import com.SpeakTrace.backend.repository.UploadRecordRepository
import com.SpeakTrace.backend.model.User // 假設 User 模型存在
import com.SpeakTrace.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

@Service
class RecordService(
    private val uploadRecordRepository: UploadRecordRepository,
    private val userRepository: UserRepository // 假設有 UserRepository
) {
    private val uploadDir = "D:/!universitiy/3-1/ApplicationSoftwareDesignPractice/final_project/project_kotlin/database/upload_audio" // 本地資料夾路徑

    init {
        val dir = File(uploadDir)
        if (!dir.exists()) dir.mkdirs()
    }

    fun saveRecord(file: MultipartFile, email: String): UploadRecord {
        val filename = file.originalFilename ?: "unknown"
        val dir = File("$uploadDir/$email")
        if (!dir.exists()) dir.mkdirs()
        val filepath = "$uploadDir/$email/$filename"
        System.out.println("filename: $filename")
        System.out.println("filepath: $filepath")

        // 儲存檔案到本地
        Files.copy(file.inputStream, Paths.get(filepath))

        // 取得用戶（假設有方法根據 email 找到用戶）
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException("用戶不存在")

        // 儲存到資料庫
        val record = UploadRecord(
            user = user,
            fileName = filename,
            filePath = filepath,
            uploadedAt = LocalDateTime.now()
        )
        return uploadRecordRepository.save(record)
    }

    fun getRecordsByEmail(email: String): List<Map<String, Any>> {
        val records = uploadRecordRepository.findByUserEmail(email)
        val results = mutableListOf<Map<String, Any>>() // 使用可變列表

        if (records.isNotEmpty()) {
            for (record in records) {
                val result = mapOf(
                    "id" to record.id,
                    "filename" to record.fileName,
                    "time" to record.uploadedAt.toString(),
                    "language" to "中文",
                    "status" to record.status
                )
                results.add(result) // 將結果加入列表
            }
        }

        return results // 返回處理後的結果
    }

    fun deleteRecords(ids: List<Long>, email: String) {
        val records = uploadRecordRepository.findAllById(ids)

        if (records.isEmpty()) {
            throw IllegalArgumentException("沒有找到對應的記錄")
        }

        for (record in records) {
            // 驗證該記錄是否屬於該用戶
            if (record.user.email != email) {
                throw IllegalArgumentException("無權限刪除記錄 ID: ${record.id}")
            }

            // 刪除本地檔案
            val file = File(record.filePath)
            if (file.exists()) {
                if (!file.delete()) {
                    throw IllegalStateException("無法刪除檔案：${record.filePath}")
                }
            }
        }

        // 從資料庫中刪除記錄
        uploadRecordRepository.deleteAll(records)
    }
}